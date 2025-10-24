// ============================================================================
// dataProcessor.js - Xử lý dữ liệu, tích hợp tất cả các module khác
// ============================================================================

const config = require("./config");
const fileHandler = require("./fileHandler");
const apiClient = require("./apiClient");
const EventEmitter = require("events");

class DataProcessor extends EventEmitter {
  constructor() {
    super();
    this.config = config;
    this.fileHandler = fileHandler;
    this.apiClient = apiClient;
    this.cache = {};
    this.processingQueue = [];
    this.isProcessing = false;
  }

  async initialize() {
    console.log("[DataProcessor] Initializing...");

    try {
      this.config.loadConfigFromFile();
      this.config.validateConfig();
      this.config.displayConfig();

      await this.checkApiHealth();
      this.loadCache();

      console.log("[DataProcessor] Initialization complete");
      this.emit("initialized");
      return true;
    } catch (error) {
      console.error(`[DataProcessor] Initialization failed: ${error.message}`);
      this.emit("error", error);
      return false;
    }
  }

  async checkApiHealth() {
    try {
      const result = await this.apiClient.checkHealth();
      console.log("[DataProcessor] API health check passed");
      return true;
    } catch (error) {
      console.warn(`[DataProcessor] API health check failed: ${error.error}`);
      return false;
    }
  }

  loadCache() {
    try {
      if (this.config.cacheEnabled) {
        const cacheContent = this.fileHandler.readFile("cache.json");
        if (cacheContent) {
          this.cache = JSON.parse(cacheContent);
          console.log(
            `[DataProcessor] Loaded cache with ${
              Object.keys(this.cache).length
            } items`
          );
        }
      }
    } catch (error) {
      console.warn(`[DataProcessor] Error loading cache: ${error.message}`);
    }
  }

  saveCache() {
    try {
      if (this.config.cacheEnabled) {
        this.fileHandler.writeFile(
          "cache.json",
          JSON.stringify(this.cache, null, 2)
        );
      }
    } catch (error) {
      console.error(`[DataProcessor] Error saving cache: ${error.message}`);
    }
  }

  async fetchData(endpoint, useCache = false) {
    console.log(`[DataProcessor] Fetching data from ${endpoint}...`);

    if (useCache && this.cache[endpoint]) {
      console.log(`[DataProcessor] Using cached data for ${endpoint}`);
      return this.cache[endpoint];
    }

    try {
      const result = await this.apiClient.get(endpoint);
      if (result.success) {
        if (useCache) {
          this.cache[endpoint] = result.data;
          this.saveCache();
        }
        return result.data;
      }
    } catch (error) {
      console.error(`[DataProcessor] Error fetching data: ${error.error}`);
    }
    return null;
  }

  async sendData(endpoint, data) {
    console.log(`[DataProcessor] Sending data to ${endpoint}...`);

    try {
      const result = await this.apiClient.post(endpoint, data);
      if (result.success) {
        console.log(`[DataProcessor] Data sent successfully`);
        return result.data;
      }
    } catch (error) {
      console.error(`[DataProcessor] Error sending data: ${error.error}`);
    }
    return null;
  }

  saveDataToFile(filename, data) {
    try {
      const jsonData =
        typeof data === "string" ? data : JSON.stringify(data, null, 2);
      const success = this.fileHandler.writeFile(filename, jsonData);

      if (success) {
        this.fileHandler.backupFile(filename);
        console.log(`[DataProcessor] Data saved to ${filename}`);
      }
      return success;
    } catch (error) {
      console.error(`[DataProcessor] Error saving data: ${error.message}`);
      return false;
    }
  }

  loadDataFromFile(filename) {
    try {
      const content = this.fileHandler.readFile(filename);
      if (content) {
        return JSON.parse(content);
      }
    } catch (error) {
      console.error(`[DataProcessor] Error loading data: ${error.message}`);
    }
    return null;
  }

  async processItem(item) {
    console.log(`[DataProcessor] Processing item: ${item.id}`);

    try {
      const endpoint = `/api/process/${item.id}`;

      item.processedAt = new Date().toISOString();
      item.status = "processing";

      const response = await this.sendData(endpoint, item);

      if (response) {
        item.status = "completed";
        item.result = response;
        console.log(`[DataProcessor] Item ${item.id} processed successfully`);
        this.emit("itemProcessed", item);
      } else {
        item.status = "failed";
        console.error(`[DataProcessor] Failed to process item ${item.id}`);
        this.emit("itemFailed", item);
      }

      return item;
    } catch (error) {
      console.error(`[DataProcessor] Error processing item: ${error.message}`);
      item.status = "error";
      item.error = error.message;
      this.emit("itemError", item);
      return item;
    }
  }

  async processBatch(items, saveToDisk = true) {
    console.log(
      `[DataProcessor] Starting batch processing of ${items.length} items...`
    );
    this.isProcessing = true;
    this.emit("batchStarted", items.length);

    const results = [];
    for (let i = 0; i < items.length; i++) {
      const item = items[i];
      console.log(`[DataProcessor] Processing item ${i + 1}/${items.length}`);

      const processed = await this.processItem(item);
      results.push(processed);

      if (saveToDisk && (i + 1) % 5 === 0) {
        this.saveDataToFile(`batch-progress-${Date.now()}.json`, {
          total: items.length,
          processed: i + 1,
          items: results,
        });
      }
    }

    this.isProcessing = false;

    if (saveToDisk) {
      this.saveDataToFile(`batch-result-${Date.now()}.json`, {
        totalItems: items.length,
        completedItems: results.filter((r) => r.status === "completed").length,
        failedItems: results.filter((r) => r.status === "failed").length,
        results: results,
      });
    }

    console.log(`[DataProcessor] Batch processing completed`);
    this.emit("batchCompleted", results);

    return results;
  }

  getStatistics() {
    return {
      cacheSize: Object.keys(this.cache).length,
      isProcessing: this.isProcessing,
      queueSize: this.processingQueue.length,
      config: {
        env: this.config.env,
        apiUrl: this.config.apiUrl,
        cacheEnabled: this.config.cacheEnabled,
        timeout: this.config.timeout,
        maxRetries: this.config.maxRetries,
      },
    };
  }

  async cleanup() {
    console.log("[DataProcessor] Cleaning up...");
    this.saveCache();
    this.fileHandler.deleteFile("temp-*.json");
    console.log("[DataProcessor] Cleanup complete");
  }
}

module.exports = new DataProcessor();

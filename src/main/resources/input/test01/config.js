// ============================================================================
// config.js - Quản lý cấu hình và biến môi trường
// ============================================================================

const fs = require("fs");
const path = require("path");

class Config {
  constructor() {
    this.env = process.env.NODE_ENV || "development";
    this.apiUrl = process.env.API_URL || "http://localhost:3000";
    this.apiKey = process.env.API_KEY || "default-key-12345";
    this.dataDir = process.env.DATA_DIR || "./data";
    this.logLevel = process.env.LOG_LEVEL || "info";
    this.timeout = parseInt(process.env.TIMEOUT, 10) || 5000;
    this.maxRetries = parseInt(process.env.MAX_RETRIES, 10) || 3;
    this.cacheEnabled = process.env.CACHE_ENABLED === "true";
    this.configPath = process.env.CONFIG_PATH || "./config.json";
    this.dbHost = process.env.DB_HOST || "localhost";
    this.dbPort = parseInt(process.env.DB_PORT, 10) || 5432;
  }

  loadConfigFromFile() {
    try {
      if (fs.existsSync(this.configPath)) {
        const configContent = fs.readFileSync(this.configPath, "utf-8");
        const configJson = JSON.parse(configContent);
        Object.assign(this, configJson);
        console.log(`[Config] Loaded configuration from ${this.configPath}`);
      }
    } catch (error) {
      console.error(`[Config] Error loading config file: ${error.message}`);
    }
  }

  getConfig(key) {
    return this[key];
  }

  setConfig(key, value) {
    this[key] = value;
  }

  validateConfig() {
    const required = ["apiUrl", "apiKey", "dataDir"];
    for (const key of required) {
      if (!this[key]) {
        throw new Error(`[Config] Missing required configuration: ${key}`);
      }
    }
  }

  displayConfig() {
    console.log("[Config] Current Configuration:");
    console.log(`  Environment: ${this.env}`);
    console.log(`  API URL: ${this.apiUrl}`);
    console.log(`  API Key: ${this.apiKey.substring(0, 5)}***`);
    console.log(`  Data Directory: ${this.dataDir}`);
    console.log(`  Log Level: ${this.logLevel}`);
    console.log(`  Timeout: ${this.timeout}ms`);
    console.log(`  Cache Enabled: ${this.cacheEnabled}`);
  }
}

module.exports = new Config();

// ============================================================================
// apiClient.js - Giao tiếp với mạng (HTTP requests)
// ============================================================================

const http = require("http");
const https = require("https");
const url = require("url");
const config = require("./config");

class ApiClient {
  constructor() {
    this.timeout = config.timeout;
    this.maxRetries = config.maxRetries;
    this.apiKey = config.apiKey;
    this.baseUrl = config.apiUrl;
  }

  makeRequest(method, endpoint, data = null, retryCount = 0) {
    return new Promise((resolve, reject) => {
      try {
        const fullUrl = `${this.baseUrl}${endpoint}`;
        const urlObj = new url.URL(fullUrl);
        const protocol = urlUrl.protocol === "https:" ? https : http;

        const options = {
          method: method,
          headers: {
            "Content-Type": "application/json",
            Authorization: `Bearer ${this.apiKey}`,
            "X-Request-ID": this.generateRequestId(),
          },
          timeout: this.timeout,
        };

        const req = protocol.request(urlObj, options, (res) => {
          let responseData = "";

          res.on("data", (chunk) => {
            responseData += chunk;
          });

          res.on("end", () => {
            console.log(
              `[ApiClient] Response from ${endpoint}: ${res.statusCode}`
            );

            if (res.statusCode >= 200 && res.statusCode < 300) {
              try {
                const parsed = JSON.parse(responseData);
                resolve({
                  success: true,
                  status: res.statusCode,
                  data: parsed,
                });
              } catch (e) {
                resolve({
                  success: true,
                  status: res.statusCode,
                  data: responseData,
                });
              }
            } else if (res.statusCode >= 500 && retryCount < this.maxRetries) {
              console.warn(
                `[ApiClient] Server error, retrying... (${retryCount + 1}/${
                  this.maxRetries
                })`
              );
              setTimeout(() => {
                this.makeRequest(method, endpoint, data, retryCount + 1)
                  .then(resolve)
                  .catch(reject);
              }, 1000 * (retryCount + 1));
            } else {
              reject({
                success: false,
                status: res.statusCode,
                error: responseData,
              });
            }
          });
        });

        req.on("timeout", () => {
          console.error(`[ApiClient] Request timeout for ${endpoint}`);
          req.destroy();
          reject({
            success: false,
            error: "Request timeout",
          });
        });

        req.on("error", (error) => {
          console.error(`[ApiClient] Request error: ${error.message}`);

          if (retryCount < this.maxRetries) {
            console.warn(
              `[ApiClient] Retrying request... (${retryCount + 1}/${
                this.maxRetries
              })`
            );
            setTimeout(() => {
              this.makeRequest(method, endpoint, data, retryCount + 1)
                .then(resolve)
                .catch(reject);
            }, 1000 * (retryCount + 1));
          } else {
            reject({
              success: false,
              error: error.message,
            });
          }
        });

        if (data) {
          req.write(JSON.stringify(data));
        }

        req.end();
      } catch (error) {
        console.error(`[ApiClient] Error preparing request: ${error.message}`);
        reject({
          success: false,
          error: error.message,
        });
      }
    });
  }

  get(endpoint) {
    console.log(`[ApiClient] GET ${endpoint}`);
    return this.makeRequest("GET", endpoint);
  }

  post(endpoint, data) {
    console.log(`[ApiClient] POST ${endpoint}`);
    return this.makeRequest("POST", endpoint, data);
  }

  put(endpoint, data) {
    console.log(`[ApiClient] PUT ${endpoint}`);
    return this.makeRequest("PUT", endpoint, data);
  }

  delete(endpoint) {
    console.log(`[ApiClient] DELETE ${endpoint}`);
    return this.makeRequest("DELETE", endpoint);
  }

  generateRequestId() {
    return `req-${Date.now()}-${Math.random().toString(36).substr(2, 9)}`;
  }

  checkHealth(endpoint = "/health") {
    return this.get(endpoint);
  }

  async retryWithBackoff(fn, maxAttempts = 3) {
    for (let i = 0; i < maxAttempts; i++) {
      try {
        return await fn();
      } catch (error) {
        if (i === maxAttempts - 1) throw error;
        const delayMs = Math.pow(2, i) * 1000;
        console.log(`[ApiClient] Retry attempt ${i + 1}, waiting ${delayMs}ms`);
        await this.delay(delayMs);
      }
    }
  }

  delay(ms) {
    return new Promise((resolve) => setTimeout(resolve, ms));
  }
}

module.exports = new ApiClient();

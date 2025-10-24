// ============================================================================
// networkCalls.js - Các lệnh gọi mạng để match regex patterns
// ============================================================================

const http = require("http");
const https = require("https");
const axios = require("axios");

// Test HTTP.GET với regex pattern
http.get("https://api.example.com/data", (res) => {
  console.log("Response:", res);
});

// Test HTTPS.GET với regex pattern
https.get("https://secure.example.com/api", (res) => {
  console.log("Response:", res);
});

// Test AXIOS.GET
axios.get("https://jsonplaceholder.typicode.com/users");

// Test AXIOS.POST
axios.post("https://api.github.com/repos", { name: "test" });

// Test AXIOS.REQUEST
axios.request({
  method: "GET",
  url: "https://api.example.com/endpoint",
});

// Test REQUEST.GET (request library)
const request = require("request");
request.get("https://example.com", (err, res, body) => {
  console.log(body);
});

// Test GOT.GET
const got = require("got");
got.get("https://example.org");

// Test DNS.RESOLVE
const dns = require("dns");
dns.resolve("example.com", (err, addresses) => {
  console.log(addresses);
});

// Test fetch
fetch("https://api.example.com/data")
  .then((res) => res.json())
  .then((data) => console.log(data));

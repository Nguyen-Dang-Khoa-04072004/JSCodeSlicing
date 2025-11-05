// input.js
// Giả lập input từ user hoặc nguồn mạng (tainted data)
const readline = require("readline");

const rl = readline.createInterface({
  input: process.stdin,
  output: process.stdout,
});

rl.question("Enter your command: ", (userInput) => {
  // userInput là dữ liệu tainted
  module.exports = { command: userInput };
  rl.close();
});

const fs = require("fs");
function main() {
  const data = fs.readFileSync("user_input.txt", "utf8");
  const trimmed = data.trim();
  eval(trimmed);
}
main();

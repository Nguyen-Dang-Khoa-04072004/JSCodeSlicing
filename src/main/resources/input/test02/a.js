// execute.js
const { command } = require("./input"); // lấy dữ liệu từ file A
const { exec } = require("child_process");

if (!command) {
  console.log("No command to execute.");
} else {
  // Tainted execution: dữ liệu từ user được exec trực tiếp
  console.log("Executing command:", command);
  exec(command, (error, stdout, stderr) => {
    if (error) {
      console.error(`Error: ${error.message}`);
      return;
    }
    if (stderr) {
      console.error(`Stderr: ${stderr}`);
      return;
    }
    console.log(`Output:\n${stdout}`);
  });
}

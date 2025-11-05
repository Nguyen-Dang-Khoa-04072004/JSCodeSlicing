// a.js
import { callFromB } from "./b.js";

function secretFunctionA(message) {
  console.log("A received:", message);
  return "Response from A";
}

// Gọi hàm trong b.js thông qua eval
export function callFromA() {
  const codeToEval = `
    import('./b.js').then(module => {
      module.callFromB("Message from A via eval");
    });
  `;
  eval(codeToEval);
}

// Khi file a.js được chạy, khởi động chuỗi gọi qua lại
callFromA();

// Cho phép b.js gọi ngược lại
export function triggerFromB() {
  console.log("B triggered A via eval");
  secretFunctionA("Called back from B");
}

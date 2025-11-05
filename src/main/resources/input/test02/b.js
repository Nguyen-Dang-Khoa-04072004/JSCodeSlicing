// b.js
import { triggerFromB } from "./a.js";

function secretFunctionB(message) {
  console.log("B received:", message);
  return "Response from B";
}

// Gọi lại A qua eval
export function callFromB(msg) {
  console.log("B processing:", msg);
  const codeToEval = `
    import('./a.js').then(module => {
      module.triggerFromB();
    });
  `;
  eval(codeToEval);
}

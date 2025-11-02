// renderer.js
// Module này chịu trách nhiệm tạo HTML từ dữ liệu đầu vào
function renderPage(name) {
  // NGUY HIỂM: chèn trực tiếp dữ liệu user vào HTML (không escape)
  return `
    <!doctype html>
    <html>
      <head><meta charset="utf-8"><title>Welcome</title></head>
      <body>
        <h1>Xin chào, ${name}!</h1>
        <p>Chúc bạn một ngày tốt lành.</p>
      </body>
    </html>
  `;
}

module.exports = { renderPage };

// ============================================================================
// main.js - Điểm khởi đầu của ứng dụng
// ============================================================================

const dataProcessor = require("./dataProcessor");

// Định nghĩa dữ liệu mẫu
const sampleData = [
  { id: 1, name: "Task 1", priority: "high", data: { value: 100 } },
  { id: 2, name: "Task 2", priority: "medium", data: { value: 200 } },
  { id: 3, name: "Task 3", priority: "low", data: { value: 300 } },
  { id: 4, name: "Task 4", priority: "high", data: { value: 400 } },
  { id: 5, name: "Task 5", priority: "medium", data: { value: 500 } },
];

async function main() {
  console.log("========================================");
  console.log("Starting Data Processor Application");
  console.log("========================================\n");

  try {
    // Khởi tạo ứng dụng
    const initialized = await dataProcessor.initialize();
    if (!initialized) {
      console.error("Failed to initialize application");
      process.exit(1);
    }

    // Lắng nghe các sự kiện
    dataProcessor.on("itemProcessed", (item) => {
      console.log(`✓ Item ${item.id} completed`);
    });

    dataProcessor.on("itemFailed", (item) => {
      console.log(`✗ Item ${item.id} failed`);
    });

    dataProcessor.on("batchCompleted", (results) => {
      console.log(
        `\n✓ Batch processing completed with ${results.length} items`
      );
    });

    // Lưu dữ liệu mẫu
    console.log("\n--- Saving Sample Data ---");
    dataProcessor.saveDataToFile("input-data.json", sampleData);

    // Lấy thống kê
    console.log("\n--- Application Statistics ---");
    const stats = dataProcessor.getStatistics();
    console.log(JSON.stringify(stats, null, 2));

    // Xử lý batch
    console.log("\n--- Processing Batch ---");
    const results = await dataProcessor.processBatch(sampleData, true);

    // Hiển thị kết quả
    console.log("\n--- Results Summary ---");
    const completed = results.filter((r) => r.status === "completed").length;
    const failed = results.filter((r) => r.status === "failed").length;
    console.log(
      `Total: ${results.length}, Completed: ${completed}, Failed: ${failed}`
    );

    // Dọn dẹp
    console.log("\n--- Cleaning Up ---");
    await dataProcessor.cleanup();

    console.log("\n========================================");
    console.log("Application finished successfully");
    console.log("========================================");
  } catch (error) {
    console.error("Fatal error:", error.message);
    process.exit(1);
  }
}

// Xử lý tín hiệu dừng
process.on("SIGINT", async () => {
  console.log("\n\nReceived SIGINT, cleaning up...");
  await dataProcessor.cleanup();
  process.exit(0);
});

process.on("SIGTERM", async () => {
  console.log("\n\nReceived SIGTERM, cleaning up...");
  await dataProcessor.cleanup();
  process.exit(0);
});

// Chạy ứng dụng
main().catch((error) => {
  console.error("Unhandled error:", error);
  process.exit(1);
});

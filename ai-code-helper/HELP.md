# 终端 1：启动后端
cd ai-code-helper
mvn spring-boot:run

# 终端 2：启动前端（当前已在 http://localhost:5173 运行）
cd ai-code-helper-frontend
npm run dev
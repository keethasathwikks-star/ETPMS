# ETPMS AI Service - Quick Start Guide

**Get the complete AI analytics system up and running in 10 minutes.**

---

## ⚡ 5-Minute Setup

### Prerequisites

✅ **Already Installed:**
- Java 11+ (for Spring Boot backend - already running)
- MySQL (for database - already running)
- Node.js & npm (for React frontend - already running)

❓ **Need to Install:**
- Python 3.8+ ([Download](https://www.python.org/downloads/))

Check if Python is installed:
```bash
python --version
```

---

## 🚀 Step-by-Step Setup

### Step 1: Navigate to AI Service Folder

```bash
cd c:\TTPROJECT\ETPMS\ai-service
```

### Step 2: Create Virtual Environment (Recommended)

```bash
# Windows
python -m venv venv
venv\Scripts\activate

# Mac/Linux
python3 -m venv venv
source venv/bin/activate
```

### Step 3: Install Dependencies

```bash
pip install -r requirements.txt
```

**Time:** ~2-3 minutes (downloads packages)

### Step 4: Train Machine Learning Models

```bash
python train_models.py
```

**Expected output:**
```
============================================================
ETPMS AI SERVICE: MODEL TRAINING
============================================================

Generating sample training data...
✓ Generated synthetic data for 100 employees

Training models...

[1/5] Training Performance Predictor (Linear Regression)...
✓ Performance Predictor trained | R² Score: 0.8234

[2/5] Training Task Completion Predictor (Logistic Regression)...
✓ Task Completion Predictor trained | Accuracy: 0.7100

[3/5] Training Employee Clusterer (K-Means)...
✓ Employee Clusterer trained | Inertia: 45.5432

[4/5] Training Employee Recommender (Nearest Neighbors)...
✓ Employee Recommender trained | K-Neighbors: 5

[5/5] Saving training metadata...
✓ Metadata saved

============================================================
✓ ALL MODELS TRAINED SUCCESSFULLY
✓ Models saved to: c:\TTPROJECT\ETPMS\ai-service\models
============================================================
```

**Time:** ~30 seconds

### Step 5: Start AI Service

```bash
python -m uvicorn main:app --host 0.0.0.0 --port 8000 --reload
```

**Expected output:**
```
INFO:     Uvicorn running on http://0.0.0.0:8000 (Press CTRL+C to quit)
INFO:     Application startup complete
```

The service is now running at: **http://localhost:8000**

---

## ✅ Verify Everything Works

### Test 1: Check AI Service Health

In a new terminal:
```bash
curl http://localhost:8000/health
```

**Expected response:**
```json
{
  "status": "healthy",
  "models_available": true,
  "message": "All models loaded"
}
```

### Test 2: Test Performance Prediction

```bash
curl -X POST http://localhost:8000/predict-performance \
  -H "Content-Type: application/json" \
  -d '{
    "employee_id": 1,
    "tasks_assigned": 10,
    "tasks_completed": 9,
    "missed_deadlines": 1,
    "avg_completion_time": 12.5
  }'
```

**Expected response:**
```json
{
  "employee_id": 1,
  "predicted_score": 78.45
}
```

### Test 3: Test via Spring Boot Backend

Make sure backend is running, then:
```bash
curl -X POST http://localhost:8080/api/ai/performance/1
```

**Expected response:**
```json
{
  "employeeId": 1,
  "predictedScore": 78.45
}
```

### Test 4: Check Your Database

Verify data exists:
```bash
mysql -u root -proot etpms_db -e "SELECT COUNT(*) as employee_count FROM employees; SELECT COUNT(*) as task_count FROM tasks;"
```

---

## 🎯 All Endpoints Quick Reference

### Direct AI Service (Port 8000)

| Method | Endpoint | Purpose |
|--------|----------|---------|
| GET | `/health` | Check service status |
| POST | `/predict-performance` | Get performance score |
| POST | `/predict-task-completion` | Predict task status |
| POST | `/employee-clusters` | Cluster employees |
| POST | `/recommend-employee` | Recommend for task |
| POST | `/calculate-performance-scores` | Auto-calculate scores |

### Via Spring Boot Backend (Port 8080)

| Method | Endpoint | Purpose |
|--------|----------|---------|
| GET | `/api/ai/health` | Check AI service |
| POST | `/api/ai/performance/{employeeId}` | Performance prediction |
| POST | `/api/ai/task-prediction/{taskId}` | Task prediction |
| GET | `/api/ai/employee-clusters` | Employee clusters |
| POST | `/api/ai/recommend-employee/{taskId}` | Task recommendation |
| GET | `/api/ai/performance-scores` | All performance scores |

---

## 📁 What Was Created

```
ai-service/
├── models/                    # ⭐ ML models (auto-generated)
│   ├── performance_predictor.pkl
│   ├── task_predictor.pkl
│   ├── employee_clusterer.pkl
│   ├── employee_recommender.pkl
│   └── metadata.json
│
├── utils/                     # Helper functions
│   ├── data_loader.py
│   └── performance_calculator.py
│
├── main.py                    # ⭐ FastAPI app
├── train_models.py            # ⭐ Training script
├── requirements.txt           # ⭐ Dependencies
├── README.md                  # Full documentation
├── API_DOCUMENTATION.md       # API reference
└── EXAMPLES_AND_INTEGRATION.md # Code examples
```

### Spring Boot Files Added

```
BACKEND/ETPMS/src/main/java/com/example/ETPMS/
├── config/RestClientConfig.java             # ⭐ NEW
├── controller/AiAnalyticsController.java    # ⭐ NEW
├── dto/AIResponseDTOs.java                  # ⭐ NEW
└── service/AiAnalyticsService.java          # ⭐ NEW

BACKEND/ETPMS/src/main/resources/
└── application.properties                    # MODIFIED
```

---

## 🔧 Troubleshooting

### Issue: "ModuleNotFoundError" when running train_models.py

**Solution:**
```bash
pip install -r requirements.txt --upgrade
python train_models.py
```

---

### Issue: "Port 8000 already in use"

**Find and kill process:**
```bash
# Windows
netstat -ano | findstr :8000
taskkill /PID <PID> /F

# Mac/Linux
lsof -i :8000
kill -9 <PID>
```

Then restart:
```bash
python -m uvicorn main:app --host 0.0.0.0 --port 8000
```

---

### Issue: "AI service unavailable" from Spring Boot

**Checklist:**
1. ✅ AI service running on port 8000? `curl http://localhost:8000/health`
2. ✅ Models trained? Check `models/` folder has `.pkl` files
3. ✅ Spring Boot `application.properties` has `ai.service.url=http://localhost:8000`
4. ✅ Spring Boot restarted after property changes?

---

### Issue: "Model not found" error

**Solution:**
```bash
cd c:\TTPROJECT\ETPMS\ai-service
python train_models.py
```

Verify models were created:
```bash
ls -la models/
```

Should show:
```
performance_predictor.pkl
performance_scaler.pkl
task_predictor.pkl
task_scaler.pkl
employee_clusterer.pkl
clustering_scaler.pkl
employee_recommender.pkl
recommender_scaler.pkl
metadata.json
```

---

## 🎓 Next Steps

1. **Explore the API:**
   - Read [API_DOCUMENTATION.md](API_DOCUMENTATION.md) for complete endpoint reference
   
2. **See Code Examples:**
   - Check [EXAMPLES_AND_INTEGRATION.md](EXAMPLES_AND_INTEGRATION.md) for Python, JavaScript, and Java examples
   
3. **Access Analytics in Frontend:**
   - Update React dashboard to call `/api/ai/*` endpoints
   - Display predictions in employee/task views
   
4. **Train with Real Data:**
   - Replace synthetic training data in `train_models.py` with your database
   - Retrain models with `python train_models.py`

5. **Customize Models:**
   - Edit `train_models.py` to adjust algorithms
   - Modify feature engineering in `utils/data_loader.py`
   - Update response logic in `utils/performance_calculator.py`

---

## 📞 Support

### Common Questions

**Q: Can I train models with my actual data?**  
A: Yes! Update `train_models.py` to load from your MySQL database instead of generating synthetic data.

**Q: How do I change the AI service port?**  
A: Change `8000` in the startup command: `python -m uvicorn main:app --port 9000`

**Q: Can I deploy this in production?**  
A: Yes! See `README.md` for Docker and cloud deployment options.

**Q: How do I update models?**  
A: Rerun `python train_models.py` with new training data.

**Q: What if I want to add more prediction types?**  
A: Add new endpoints in `main.py` following the existing patterns.

---

## 🎉 You're All Set!

Your ETPMS system now has AI-powered analytics!

**Your architecture:**
```
React Frontend (Port 3000)
    ↓
Spring Boot Backend (Port 8080)
    ↓
Python AI Service (Port 8000)
    ↓
MySQL Database
```

---

**Now proceed to:**
1. Read full documentation in [README.md](README.md)
2. Review API reference in [API_DOCUMENTATION.md](API_DOCUMENTATION.md)
3. Check integration examples in [EXAMPLES_AND_INTEGRATION.md](EXAMPLES_AND_INTEGRATION.md)

---

**Version:** 1.0.0  
**Last Updated:** March 16, 2026  
**Setup Time:** ~5 minutes ⏱️

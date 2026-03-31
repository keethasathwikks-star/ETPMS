import React, { useEffect, useState } from 'react';
import {
  checkAiHealth,
  predictPerformance,
  predictTaskCompletion,
  getEmployeeClusters,
  recommendEmployee,
  getPerformanceScores,
  getAllEmployees,
  getAllTasks
} from '../api';
import './AIAnalytics.css';

const AI_HEALTH_RETRY_DELAYS = [0, 8000, 15000];

function AIAnalyticsRender() {
  const [aiHealth, setAiHealth] = useState({
    status: 'loading',
    message: 'Checking AI service...'
  });
  const [loading, setLoading] = useState(false);
  const [employees, setEmployees] = useState([]);
  const [tasks, setTasks] = useState([]);
  const [activeTab, setActiveTab] = useState('performance');
  const [predictions, setPredictions] = useState({});
  const [clusters, setClusters] = useState(null);
  const [scores, setScores] = useState(null);
  const [selectedEmployee, setSelectedEmployee] = useState('');
  const [selectedTask, setSelectedTask] = useState('');

  useEffect(() => {
    let cancelled = false;

    const checkHealthWithRetry = async () => {
      for (let attempt = 0; attempt < AI_HEALTH_RETRY_DELAYS.length; attempt += 1) {
        if (cancelled) {
          return;
        }

        const delay = AI_HEALTH_RETRY_DELAYS[attempt];
        if (delay > 0) {
          setAiHealth({
            status: 'warming',
            message: 'AI service is waking up on Render. This can take a few seconds.'
          });
          await new Promise((resolve) => setTimeout(resolve, delay));
          if (cancelled) {
            return;
          }
        } else {
          setAiHealth({
            status: 'loading',
            message: 'Checking AI service...'
          });
        }

        try {
          const response = await checkAiHealth();
          if (!cancelled) {
            setAiHealth(response.data);
          }
          return;
        } catch (error) {
          if (attempt === AI_HEALTH_RETRY_DELAYS.length - 1 && !cancelled) {
            setAiHealth({
              status: 'error',
              message: 'AI service is unavailable right now. If it is sleeping, refresh in a few seconds.'
            });
          }
        }
      }
    };

    checkHealthWithRetry();
    loadInitialData();

    return () => {
      cancelled = true;
    };
  }, []);

  const loadInitialData = async () => {
    try {
      const [empRes, taskRes] = await Promise.all([getAllEmployees(), getAllTasks()]);
      setEmployees(empRes.data || []);
      setTasks(taskRes.data || []);
    } catch (error) {
      console.error('Error loading data:', error);
    }
  };

  const refreshAiHealth = async () => {
    setAiHealth({
      status: 'loading',
      message: 'Checking AI service...'
    });

    try {
      const response = await checkAiHealth();
      setAiHealth(response.data);
    } catch (error) {
      setAiHealth({
        status: 'warming',
        message: 'AI service may still be waking up. Try again in a few seconds.'
      });
    }
  };

  const handlePredictPerformance = async () => {
    if (!selectedEmployee) {
      alert('Please select an employee');
      return;
    }

    setLoading(true);
    try {
      const response = await predictPerformance(selectedEmployee);
      setPredictions((prev) => ({
        ...prev,
        performance: response.data
      }));
    } catch (error) {
      alert('Error predicting performance: ' + error.message);
    } finally {
      setLoading(false);
    }
  };

  const handlePredictTask = async () => {
    if (!selectedTask) {
      alert('Please select a task');
      return;
    }

    setLoading(true);
    try {
      const response = await predictTaskCompletion(selectedTask);
      setPredictions((prev) => ({
        ...prev,
        taskCompletion: response.data
      }));
    } catch (error) {
      alert('Error predicting task: ' + error.message);
    } finally {
      setLoading(false);
    }
  };

  const handleGetClusters = async () => {
    setLoading(true);
    try {
      const response = await getEmployeeClusters();
      setClusters(response.data);
    } catch (error) {
      alert('Error fetching clusters: ' + error.message);
    } finally {
      setLoading(false);
    }
  };

  const handleGetScores = async () => {
    setLoading(true);
    try {
      const response = await getPerformanceScores();
      setScores(response.data);
    } catch (error) {
      alert('Error fetching scores: ' + error.message);
    } finally {
      setLoading(false);
    }
  };

  const handleRecommend = async () => {
    if (!selectedTask) {
      alert('Please select a task');
      return;
    }

    setLoading(true);
    try {
      const response = await recommendEmployee(selectedTask);
      setPredictions((prev) => ({
        ...prev,
        recommendation: response.data
      }));
    } catch (error) {
      alert('Error getting recommendation: ' + error.message);
    } finally {
      setLoading(false);
    }
  };

  const aiStatusLabel =
    aiHealth?.status === 'healthy'
      ? 'Online'
      : aiHealth?.status === 'warming'
        ? 'Waking Up'
        : aiHealth?.status === 'error'
          ? 'Offline'
          : 'Checking';

  const aiBadgeLabel =
    aiHealth?.status === 'healthy'
      ? 'Healthy'
      : aiHealth?.status === 'warming'
        ? 'Waking up...'
        : aiHealth?.status === 'error'
          ? 'Unavailable'
          : 'Checking...';

  return (
    <div className="ai-analytics-container">
      <div className="dashboard-hero">
        <div className="hero-title">
          <div className="hero-icon">AI</div>
          <div>
            <h1>AI Analytics Dashboard</h1>
            <p className="hero-subtitle">
              Turn employee and task data into actionable insights with AI-powered predictions,
              clustering, and recommendations.
            </p>
          </div>
        </div>

        <div className="overview-cards">
          <div className="overview-card">
            <span className="overview-label">Employees</span>
            <strong>{employees.length}</strong>
          </div>
          <div className="overview-card">
            <span className="overview-label">Tasks</span>
            <strong>{tasks.length}</strong>
          </div>
          <div className="overview-card">
            <span className="overview-label">AI Status</span>
            <strong>{aiStatusLabel}</strong>
          </div>
        </div>
      </div>

      <div className={`health-status ${aiHealth?.status || 'loading'}`}>
        <h3>AI Service Status</h3>
        <p className={`status-badge ${aiHealth?.status || 'loading'}`}>{aiBadgeLabel}</p>
        <p className="health-message">{aiHealth?.message}</p>
        {aiHealth?.status !== 'healthy' && (
          <button className="secondary-btn" onClick={refreshAiHealth}>
            Retry Health Check
          </button>
        )}
      </div>

      <div className="tabs">
        <button
          className={`tab-btn ${activeTab === 'performance' ? 'active' : ''}`}
          onClick={() => setActiveTab('performance')}
        >
          Performance Prediction
        </button>
        <button
          className={`tab-btn ${activeTab === 'tasks' ? 'active' : ''}`}
          onClick={() => setActiveTab('tasks')}
        >
          Task Completion
        </button>
        <button
          className={`tab-btn ${activeTab === 'clustering' ? 'active' : ''}`}
          onClick={() => setActiveTab('clustering')}
        >
          Employee Clusters
        </button>
        <button
          className={`tab-btn ${activeTab === 'recommendation' ? 'active' : ''}`}
          onClick={() => setActiveTab('recommendation')}
        >
          Recommendations
        </button>
        <button
          className={`tab-btn ${activeTab === 'scores' ? 'active' : ''}`}
          onClick={() => setActiveTab('scores')}
        >
          Performance Scores
        </button>
      </div>

      <div className="tab-content">
        {activeTab === 'performance' && (
          <div className="tab-pane">
            <h2>Predict Employee Performance</h2>
            <div className="input-group">
              <label>Select Employee:</label>
              <select value={selectedEmployee} onChange={(e) => setSelectedEmployee(e.target.value)}>
                <option value="">-- Select Employee --</option>
                {employees.map((emp) => (
                  <option key={emp.id} value={emp.id}>
                    {emp.name}
                  </option>
                ))}
              </select>
              <button className="primary-btn" onClick={handlePredictPerformance} disabled={loading}>
                {loading ? 'Predicting...' : 'Predict Performance'}
              </button>
            </div>

            {predictions.performance && (
              <div className="result-card">
                <h3>Prediction Result</h3>
                <p><strong>Employee:</strong> {predictions.performance.employeeId}</p>
                <p><strong>Predicted Score:</strong> {predictions.performance.predictedScore?.toFixed(2) || 'N/A'}</p>
              </div>
            )}
          </div>
        )}

        {activeTab === 'tasks' && (
          <div className="tab-pane">
            <h2>Predict Task Completion</h2>
            <div className="input-group">
              <label>Select Task:</label>
              <select value={selectedTask} onChange={(e) => setSelectedTask(e.target.value)}>
                <option value="">-- Select Task --</option>
                {tasks.map((task) => (
                  <option key={task.id} value={task.id}>
                    {task.title}
                  </option>
                ))}
              </select>
              <button className="primary-btn" onClick={handlePredictTask} disabled={loading}>
                {loading ? 'Predicting...' : 'Predict Completion'}
              </button>
            </div>

            {predictions.taskCompletion && (
              <div className="result-card">
                <h3>Completion Prediction</h3>
                <p><strong>Task ID:</strong> {predictions.taskCompletion.taskId}</p>
                <p><strong>Prediction:</strong> {predictions.taskCompletion.prediction}</p>
                <p><strong>Confidence:</strong> {(predictions.taskCompletion.confidence * 100)?.toFixed(1)}%</p>
              </div>
            )}
          </div>
        )}

        {activeTab === 'clustering' && (
          <div className="tab-pane">
            <h2>Employee Performance Clusters</h2>
            <button className="primary-btn" onClick={handleGetClusters} disabled={loading}>
              {loading ? 'Loading...' : 'Analyze Clusters'}
            </button>

            {clusters && clusters.clusters && (
              <div className="results-grid">
                {clusters.clusters.map((item, idx) => (
                  <div key={idx} className="cluster-card">
                    <h4>Employee {item.employeeId}</h4>
                    <p className={`cluster-badge ${item.cluster?.toLowerCase()}`}>
                      {item.cluster}
                    </p>
                  </div>
                ))}
              </div>
            )}
          </div>
        )}

        {activeTab === 'recommendation' && (
          <div className="tab-pane">
            <h2>Get Employee Recommendation</h2>
            <div className="input-group">
              <label>Select Task:</label>
              <select value={selectedTask} onChange={(e) => setSelectedTask(e.target.value)}>
                <option value="">-- Select Task --</option>
                {tasks.map((task) => (
                  <option key={task.id} value={task.id}>
                    {task.title}
                  </option>
                ))}
              </select>
              <button className="primary-btn" onClick={handleRecommend} disabled={loading}>
                {loading ? 'Recommending...' : 'Get Recommendation'}
              </button>
            </div>

            {predictions.recommendation && (
              <div className="result-card">
                <h3>Recommended Employee</h3>
                <p><strong>Task ID:</strong> {predictions.recommendation.taskId}</p>
                <p><strong>Recommended Employee:</strong> {predictions.recommendation.recommendedEmployee}</p>
                <p><strong>Score:</strong> {predictions.recommendation.recommendationScore?.toFixed(2) || 'N/A'}</p>
              </div>
            )}
          </div>
        )}

        {activeTab === 'scores' && (
          <div className="tab-pane">
            <h2>Calculate Performance Scores</h2>
            <button className="primary-btn" onClick={handleGetScores} disabled={loading}>
              {loading ? 'Calculating...' : 'Calculate All Scores'}
            </button>

            {scores && scores.scores && (
              <div className="scores-list">
                {scores.scores.map((score, idx) => (
                  <div key={idx} className="score-item">
                    <span>Employee {score.employeeId}</span>
                    <div className="score-bar">
                      <div
                        className="score-fill"
                        style={{ width: `${Math.min(score.performanceScore, 100)}%` }}
                      />
                    </div>
                    <span className="score-value">{score.performanceScore?.toFixed(1) || 0} pts</span>
                  </div>
                ))}
              </div>
            )}
          </div>
        )}
      </div>
    </div>
  );
}

export default AIAnalyticsRender;

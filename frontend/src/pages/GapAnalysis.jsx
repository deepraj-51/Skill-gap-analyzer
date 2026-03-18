import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { authService } from '../services/authService';
import { analysisService } from '../services/analysisService';
import { Chart as ChartJS, ArcElement, Tooltip, Legend, CategoryScale, LinearScale, BarElement } from 'chart.js';
import { Doughnut, Bar } from 'react-chartjs-2';
import './GapAnalysis.css';

ChartJS.register(ArcElement, Tooltip, Legend, CategoryScale, LinearScale, BarElement);

const proficiencyLabels = { 1: 'Beginner', 2: 'Intermediate', 3: 'Advanced', 4: 'Expert' };
const getProfLabel = (level) => proficiencyLabels[level] || proficiencyLabels[Math.min(level, 4)];

const GapAnalysis = () => {
  const navigate = useNavigate();
  const user = authService.getCurrentUser();
  const [roles, setRoles] = useState([]);
  const [selectedRole, setSelectedRole] = useState('');
  const [analysisResult, setAnalysisResult] = useState(null);
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    loadRoles();
  }, []);

  const loadRoles = async () => {
    try {
      const rolesData = await analysisService.getAllRoles();
      setRoles(rolesData);
    } catch (error) {
      console.error('Error loading roles:', error);
    }
  };

  const handleAnalyze = async () => {
    if (!selectedRole) {
      alert('Please select a role');
      return;
    }

    setLoading(true);
    try {
      const result = await analysisService.analyzeGap(user.userId, selectedRole);
      setAnalysisResult(result);
    } catch (error) {
      alert('Failed to perform analysis');
    } finally {
      setLoading(false);
    }
  };

  const getChartData = () => {
    if (!analysisResult) return null;

    return {
      labels: ['Matched', 'Partial', 'Missing'],
      datasets: [
        {
          data: [
            analysisResult.matchedSkills.length,
            analysisResult.partialSkills.length,
            analysisResult.missingSkills.length,
          ],
          backgroundColor: ['#10b981', '#f59e0b', '#ef4444'],
          borderColor: ['#059669', '#d97706', '#dc2626'],
          borderWidth: 2,
        },
      ],
    };
  };

  const getBarChartData = () => {
    if (!analysisResult) return null;

    const allSkills = [
      ...analysisResult.matchedSkills,
      ...analysisResult.partialSkills,
      ...analysisResult.missingSkills,
    ];

    return {
      labels: allSkills.map((s) => s.skillName),
      datasets: [
        {
          label: 'Your Level',
          data: allSkills.map((s) => s.userLevel),
          backgroundColor: '#6366f1',
        },
        {
          label: 'Required Level',
          data: allSkills.map((s) => s.requiredLevel),
          backgroundColor: '#818cf8',
        },
      ],
    };
  };

  return (
    <div className="container gap-analysis fade-in">
      <div className="page-header">
        <h1>Gap Analysis</h1>
        <p>Compare your skills with target job roles</p>
      </div>

      <div className="analysis-form card">
        <h2>Select Target Role</h2>
        <div className="form-row">
          <select
            value={selectedRole}
            onChange={(e) => setSelectedRole(e.target.value)}
            className="role-select"
          >
            <option value="">Choose a role...</option>
            {roles.map((role) => (
              <option key={role.id} value={role.id}>
                {role.name}
              </option>
            ))}
          </select>
          <button
            className="btn btn-primary"
            onClick={handleAnalyze}
            disabled={loading || !selectedRole}
          >
            {loading ? 'Analyzing...' : '🔍 Analyze Gap'}
          </button>
        </div>
      </div>

      {analysisResult && (
        <div className="analysis-results">
          <div className="result-header card">
            <h2>{analysisResult.roleName}</h2>
            <p>{analysisResult.roleDescription}</p>
          </div>

          <div className="charts-grid">
            <div className="chart-card card">
              <h3>Skills Distribution</h3>
              <div className="chart-container">
                <Doughnut data={getChartData()} options={{ maintainAspectRatio: true }} />
              </div>
              <div className="chart-legend">
                <div className="legend-item">
                  <span className="legend-dot matched"></span>
                  <span>Matched: {analysisResult.matchedSkills.length}</span>
                </div>
                <div className="legend-item">
                  <span className="legend-dot partial"></span>
                  <span>Partial: {analysisResult.partialSkills.length}</span>
                </div>
                <div className="legend-item">
                  <span className="legend-dot missing"></span>
                  <span>Missing: {analysisResult.missingSkills.length}</span>
                </div>
              </div>
            </div>

            <div className="chart-card card">
              <h3>Skill Levels Comparison</h3>
              <div className="chart-container">
                <Bar
                  data={getBarChartData()}
                  options={{
                    responsive: true,
                    maintainAspectRatio: true,
                    scales: {
                      y: {
                        beginAtZero: true,
                        max: 4,
                        ticks: {
                          color: '#cbd5e1',
                          callback: (val) => proficiencyLabels[val] || '',
                          stepSize: 1,
                        },
                        grid: { color: '#334155' },
                      },
                      x: {
                        ticks: { color: '#cbd5e1' },
                        grid: { color: '#334155' },
                      },
                    },
                    plugins: {
                      legend: {
                        labels: { color: '#cbd5e1' },
                      },
                    },
                  }}
                />
              </div>
            </div>
          </div>

          <div className="skills-breakdown">
            {analysisResult.matchedSkills.length > 0 && (
              <div className="skill-section card">
                <h3>✅ Matched Skills ({analysisResult.matchedSkills.length})</h3>
                <div className="skills-list">
                  {analysisResult.matchedSkills.map((skill, idx) => (
                    <div key={idx} className="skill-badge skill-matched">
                      {skill.skillName} ({getProfLabel(skill.userLevel)} / {getProfLabel(skill.requiredLevel)})
                    </div>
                  ))}
                </div>
              </div>
            )}

            {analysisResult.partialSkills.length > 0 && (
              <div className="skill-section card">
                <h3>📈 Partially Matched Skills ({analysisResult.partialSkills.length})</h3>
                <div className="skills-list">
                  {analysisResult.partialSkills.map((skill, idx) => (
                    <div key={idx} className="skill-badge skill-partial">
                      {skill.skillName} ({getProfLabel(skill.userLevel)} → {getProfLabel(skill.requiredLevel)})
                    </div>
                  ))}
                </div>
              </div>
            )}

            {analysisResult.missingSkills.length > 0 && (
              <div className="skill-section card">
                <h3>❌ Missing Skills ({analysisResult.missingSkills.length})</h3>
                <div className="skills-list">
                  {analysisResult.missingSkills.map((skill, idx) => (
                    <div key={idx} className="skill-badge skill-missing">
                      {skill.skillName} (Required: {getProfLabel(skill.requiredLevel)})
                    </div>
                  ))}
                </div>
              </div>
            )}
          </div>

          <div className="recommendations card">
            <h3>💡 Learning Recommendations</h3>
            <div className="recommendations-list">
              {analysisResult.recommendations.map((rec, idx) => (
                <p key={idx} className="recommendation-item">
                  {rec}
                </p>
              ))}
            </div>
            
            <button 
              className="btn btn-primary" 
              style={{ marginTop: '1.5rem', width: '100%', padding: '1rem', fontSize: '1.1rem', background: 'linear-gradient(135deg, #6366f1, #8b5cf6)', border: 'none', boxShadow: '0 4px 15px rgba(99, 102, 241, 0.3)' }}
              onClick={() => navigate('/learning-path', { state: { roleId: selectedRole, autoGenerate: true } })}
            >
              ✨ Generate Detailed AI Learning Path
            </button>
          </div>
        </div>
      )}
    </div>
  );
};

export default GapAnalysis;

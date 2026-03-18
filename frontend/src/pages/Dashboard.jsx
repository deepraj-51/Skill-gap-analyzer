import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { authService } from '../services/authService';
import { skillService } from '../services/skillService';
import { analysisService } from '../services/analysisService';
import './Dashboard.css';

const Dashboard = () => {
  const user = authService.getCurrentUser();
  const [userSkills, setUserSkills] = useState([]);
  const [roles, setRoles] = useState([]);

  const proficiencyLabels = { 1: 'Beginner', 2: 'Intermediate', 3: 'Advanced', 4: 'Expert' };
  const getProfLabel = (level) => proficiencyLabels[level] || proficiencyLabels[Math.min(level, 4)];
  const [loading, setLoading] = useState(true);
  const [greeting, setGreeting] = useState('');

  useEffect(() => {
    const hour = new Date().getHours();
    if (hour < 12) setGreeting('Good Morning');
    else if (hour < 18) setGreeting('Good Afternoon');
    else setGreeting('Good Evening');
    loadDashboardData();
  }, []);

  const loadDashboardData = async () => {
    try {
      const [skillsData, rolesData] = await Promise.all([
        skillService.getUserSkills(user.userId),
        analysisService.getAllRoles(),
      ]);
      setUserSkills(skillsData);
      setRoles(rolesData);
    } catch (error) {
      console.error('Error loading dashboard:', error);
    } finally {
      setLoading(false);
    }
  };

  const avgProficiency = userSkills.length > 0
    ? (userSkills.reduce((sum, s) => sum + Math.min(s.proficiencyLevel, 4), 0) / userSkills.length)
    : 0;

  const skillsByCategory = userSkills.reduce((acc, skill) => {
    const cat = skill.skillCategory || 'Other';
    acc[cat] = (acc[cat] || 0) + 1;
    return acc;
  }, {});

  const categoryColors = {
    Programming: '#6366f1',
    Frontend: '#8b5cf6',
    Backend: '#a855f7',
    Database: '#ec4899',
    DevOps: '#f43f5e',
    Cloud: '#f97316',
    'Data Science': '#14b8a6',
    Other: '#64748b',
  };

  if (loading) {
    return (
      <div className="dash-loading">
        <div className="dash-spinner"></div>
        <p>Loading your dashboard...</p>
      </div>
    );
  }

  return (
    <div className="dashboard-redesign">
      {/* Background effects */}
      <div className="dash-bg">
        <div className="dash-orb dash-orb-1"></div>
        <div className="dash-orb dash-orb-2"></div>
      </div>

      <div className="dash-container">
        {/* Welcome Section */}
        <section className="dash-welcome fade-in">
          <div className="welcome-text">
            <p className="welcome-greeting">{greeting} 👋</p>
            <h1>{user?.username}</h1>
            <p className="welcome-sub">Here's your skill development overview</p>
          </div>
          <div className="welcome-actions">
            <Link to="/skills" className="wa-btn wa-btn-primary">
              <span>➕</span> Add Skills
            </Link>
            <Link to="/analysis" className="wa-btn wa-btn-secondary">
              <span>🔍</span> Run Analysis
            </Link>
          </div>
        </section>

        {/* Stats Row */}
        <section className="dash-stats fade-in">
          <div className="stat-card-new stat-gradient-1">
            <div className="stat-card-icon">📚</div>
            <div className="stat-card-info">
              <span className="stat-card-num">{userSkills.length}</span>
              <span className="stat-card-label">Skills Tracked</span>
            </div>
            <div className="stat-card-ring">
              <svg viewBox="0 0 36 36">
                <path d="M18 2.0845 a 15.9155 15.9155 0 0 1 0 31.831 a 15.9155 15.9155 0 0 1 0 -31.831"
                  fill="none" stroke="rgba(255,255,255,0.15)" strokeWidth="3" />
                <path d="M18 2.0845 a 15.9155 15.9155 0 0 1 0 31.831 a 15.9155 15.9155 0 0 1 0 -31.831"
                  fill="none" stroke="#fff" strokeWidth="3" strokeLinecap="round"
                  strokeDasharray={`${Math.min(userSkills.length * 3.3, 100)}, 100`} />
              </svg>
            </div>
          </div>

          <div className="stat-card-new stat-gradient-2">
            <div className="stat-card-icon">⭐</div>
            <div className="stat-card-info">
              <span className="stat-card-num">{getProfLabel(Math.round(avgProficiency))}</span>
              <span className="stat-card-label">Avg Proficiency</span>
            </div>
            <div className="stat-card-ring">
              <svg viewBox="0 0 36 36">
                <path d="M18 2.0845 a 15.9155 15.9155 0 0 1 0 31.831 a 15.9155 15.9155 0 0 1 0 -31.831"
                  fill="none" stroke="rgba(255,255,255,0.15)" strokeWidth="3" />
                <path d="M18 2.0845 a 15.9155 15.9155 0 0 1 0 31.831 a 15.9155 15.9155 0 0 1 0 -31.831"
                  fill="none" stroke="#fff" strokeWidth="3" strokeLinecap="round"
                  strokeDasharray={`${avgProficiency * 20}, 100`} />
              </svg>
            </div>
          </div>

          <div className="stat-card-new stat-gradient-3">
            <div className="stat-card-icon">🎯</div>
            <div className="stat-card-info">
              <span className="stat-card-num">{roles.length}</span>
              <span className="stat-card-label">Target Roles</span>
            </div>
            <div className="stat-card-ring">
              <svg viewBox="0 0 36 36">
                <path d="M18 2.0845 a 15.9155 15.9155 0 0 1 0 31.831 a 15.9155 15.9155 0 0 1 0 -31.831"
                  fill="none" stroke="rgba(255,255,255,0.15)" strokeWidth="3" />
                <path d="M18 2.0845 a 15.9155 15.9155 0 0 1 0 31.831 a 15.9155 15.9155 0 0 1 0 -31.831"
                  fill="none" stroke="#fff" strokeWidth="3" strokeLinecap="round"
                  strokeDasharray={`${roles.length * 20}, 100`} />
              </svg>
            </div>
          </div>

          <div className="stat-card-new stat-gradient-4">
            <div className="stat-card-icon">📊</div>
            <div className="stat-card-info">
              <span className="stat-card-num">{Object.keys(skillsByCategory).length}</span>
              <span className="stat-card-label">Categories</span>
            </div>
            <div className="stat-card-ring">
              <svg viewBox="0 0 36 36">
                <path d="M18 2.0845 a 15.9155 15.9155 0 0 1 0 31.831 a 15.9155 15.9155 0 0 1 0 -31.831"
                  fill="none" stroke="rgba(255,255,255,0.15)" strokeWidth="3" />
                <path d="M18 2.0845 a 15.9155 15.9155 0 0 1 0 31.831 a 15.9155 15.9155 0 0 1 0 -31.831"
                  fill="none" stroke="#fff" strokeWidth="3" strokeLinecap="round"
                  strokeDasharray={`${Object.keys(skillsByCategory).length * 14}, 100`} />
              </svg>
            </div>
          </div>
        </section>

        {/* Main Content Grid */}
        <div className="dash-main-grid">
          {/* Skills Overview */}
          <section className="dash-card skills-overview fade-in">
            <div className="card-header">
              <h2>Your Skills</h2>
              <Link to="/skills" className="card-link">View All →</Link>
            </div>

            {userSkills.length === 0 ? (
              <div className="dash-empty">
                <div className="empty-icon">🎒</div>
                <h3>No skills added yet</h3>
                <p>Start building your profile by adding your first skill!</p>
                <Link to="/skills" className="wa-btn wa-btn-primary">Add Your First Skill</Link>
              </div>
            ) : (
              <div className="skills-scroll">
                {userSkills.map((skill) => (
                  <div key={skill.id} className="skill-row">
                    <div className="skill-row-left">
                      <span className="skill-dot" style={{ background: categoryColors[skill.skillCategory] || '#6366f1' }}></span>
                      <div>
                        <span className="skill-row-name">{skill.skillName}</span>
                        <span className="skill-row-cat">{skill.skillCategory}</span>
                      </div>
                    </div>
                    <div className="skill-row-right">
                      <div className="proficiency-bar">
                        <div className="proficiency-fill"
                          style={{
                            width: `${Math.min(skill.proficiencyLevel, 4) * 25}%`,
                            background: categoryColors[skill.skillCategory] || '#6366f1'
                          }}>
                        </div>
                      </div>
                      <span className="proficiency-num">{getProfLabel(skill.proficiencyLevel)}</span>
                    </div>
                  </div>
                ))}
              </div>
            )}
          </section>

          {/* Right Sidebar  */}
          <div className="dash-sidebar">
            {/* Category Breakdown */}
            <section className="dash-card category-card fade-in">
              <h2>Skill Categories</h2>
              {Object.keys(skillsByCategory).length === 0 ? (
                <p className="muted-text">Add skills to see category breakdown</p>
              ) : (
                <div className="category-list">
                  {Object.entries(skillsByCategory).map(([cat, count]) => (
                    <div key={cat} className="cat-row">
                      <div className="cat-info">
                        <span className="cat-dot" style={{ background: categoryColors[cat] || '#6366f1' }}></span>
                        <span className="cat-name">{cat}</span>
                      </div>
                      <div className="cat-bar-wrap">
                        <div className="cat-bar" style={{
                          width: `${(count / userSkills.length) * 100}%`,
                          background: categoryColors[cat] || '#6366f1'
                        }}></div>
                      </div>
                      <span className="cat-count">{count}</span>
                    </div>
                  ))}
                </div>
              )}
            </section>

            {/* Quick Role Cards */}
            <section className="dash-card roles-card fade-in">
              <div className="card-header">
                <h2>Explore Roles</h2>
                <Link to="/analysis" className="card-link">Analyze →</Link>
              </div>
              <div className="roles-mini-list">
                {roles.slice(0, 4).map((role, i) => (
                  <Link to="/analysis" key={role.id} className="role-mini-card"
                    style={{ '--delay': `${i * 0.05}s` }}>
                    <div className="role-mini-icon">
                      {['💻', '⚙️', '🖥️', '📊', '🚀'][i] || '💼'}
                    </div>
                    <div>
                      <span className="role-mini-name">{role.name}</span>
                      <span className="role-mini-desc">{role.description?.substring(0, 50)}...</span>
                    </div>
                  </Link>
                ))}
              </div>
            </section>
          </div>
        </div>

        {/* Bottom CTA */}
        {userSkills.length > 0 && (
          <section className="dash-cta fade-in">
            <div className="dash-cta-content">
              <div>
                <h2>Ready to analyze your gaps?</h2>
                <p>Compare your {userSkills.length} skills against industry role requirements</p>
              </div>
              <Link to="/analysis" className="wa-btn wa-btn-primary wa-btn-lg">
                🔍 Start Gap Analysis →
              </Link>
            </div>
          </section>
        )}
      </div>
    </div>
  );
};

export default Dashboard;

import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import './LandingPage.css';

const LandingPage = () => {
  const [isScrolled, setIsScrolled] = useState(false);
  const [mobileMenuOpen, setMobileMenuOpen] = useState(false);

  useEffect(() => {
    const handleScroll = () => setIsScrolled(window.scrollY > 50);
    window.addEventListener('scroll', handleScroll);
    return () => window.removeEventListener('scroll', handleScroll);
  }, []);

  const features = [
    {
      icon: '📊',
      title: 'Smart Gap Analysis',
      desc: 'Our rule-based engine compares your skills against industry-standard role requirements to identify exactly where you stand.',
    },
    {
      icon: '🎯',
      title: 'Personalized Recommendations',
      desc: 'Get prioritized, actionable learning paths tailored to your specific skill gaps and career goals.',
    },
    {
      icon: '📈',
      title: 'Visual Dashboards',
      desc: 'Interactive charts and visualizations make it easy to understand your strengths and areas for improvement.',
    },
    {
      icon: '💼',
      title: 'Role Library',
      desc: 'Explore curated job roles like Full Stack Developer, Data Scientist, DevOps Engineer and more.',
    },
    {
      icon: '⚡',
      title: 'Real-Time Tracking',
      desc: 'Track your skill proficiency levels from 1-5 and watch your readiness score improve over time.',
    },
    {
      icon: '🔒',
      title: 'Secure & Private',
      desc: 'Your data is protected with JWT authentication and industry-standard security practices.',
    },
  ];

  const steps = [
    { num: '01', title: 'Create Your Profile', desc: 'Sign up and build your skill profile by adding your current skills and proficiency levels.' },
    { num: '02', title: 'Choose a Target Role', desc: 'Browse our curated library of in-demand tech roles and select the one you want to pursue.' },
    { num: '03', title: 'Analyze Your Gaps', desc: 'Our engine compares your skills with role requirements and categorizes them as matched, partial, or missing.' },
    { num: '04', title: 'Follow Your Path', desc: 'Get personalized learning recommendations and start closing your skill gaps systematically.' },
  ];

  return (
    <div className="landing">
      {/* Navbar */}
      <nav className={`landing-nav ${isScrolled ? 'scrolled' : ''}`}>
        <div className="landing-nav-container">
          <a href="#hero" className="landing-logo">
            <span className="logo-icon">🎯</span> SkillGap Analyzer
          </a>
          <button className="mobile-toggle" onClick={() => setMobileMenuOpen(!mobileMenuOpen)}>
            {mobileMenuOpen ? '✕' : '☰'}
          </button>
          <div className={`landing-nav-links ${mobileMenuOpen ? 'open' : ''}`}>
            <a href="#features" onClick={() => setMobileMenuOpen(false)}>Features</a>
            <a href="#how-it-works" onClick={() => setMobileMenuOpen(false)}>How It Works</a>
            <a href="#roles" onClick={() => setMobileMenuOpen(false)}>Roles</a>
            <Link to="/login" className="nav-btn nav-btn-outline" onClick={() => setMobileMenuOpen(false)}>Sign In</Link>
            <Link to="/register" className="nav-btn nav-btn-primary" onClick={() => setMobileMenuOpen(false)}>Get Started</Link>
          </div>
        </div>
      </nav>

      {/* Hero Section */}
      <section className="hero" id="hero">
        <div className="hero-bg-effects">
          <div className="hero-orb hero-orb-1"></div>
          <div className="hero-orb hero-orb-2"></div>
          <div className="hero-orb hero-orb-3"></div>
        </div>
        <div className="hero-content">
          <div className="hero-badge">🚀 Adaptive Learning Platform</div>
          <h1>
            Bridge Your <span className="gradient-text">Skill Gaps</span>,<br />
            Accelerate Your <span className="gradient-text">Career</span>
          </h1>
          <p className="hero-subtitle">
            Discover exactly which skills you need, get personalized learning recommendations,
            and track your progress toward your dream tech role — all in one platform.
          </p>
          <div className="hero-cta">
            <Link to="/register" className="btn-hero btn-hero-primary">
              Start Free Analysis →
            </Link>
            <a href="#how-it-works" className="btn-hero btn-hero-secondary">
              See How It Works
            </a>
          </div>
          <div className="hero-stats">
            <div className="hero-stat">
              <span className="hero-stat-num">30+</span>
              <span className="hero-stat-label">Skills Tracked</span>
            </div>
            <div className="hero-stat-divider"></div>
            <div className="hero-stat">
              <span className="hero-stat-num">5+</span>
              <span className="hero-stat-label">Job Roles</span>
            </div>
            <div className="hero-stat-divider"></div>
            <div className="hero-stat">
              <span className="hero-stat-num">100%</span>
              <span className="hero-stat-label">Free to Use</span>
            </div>
          </div>
        </div>
      </section>

      {/* Features Section */}
      <section className="features-section" id="features">
        <div className="section-container">
          <div className="section-header">
            <span className="section-tag">Features</span>
            <h2>Everything You Need to <span className="gradient-text">Level Up</span></h2>
            <p>Powerful tools designed to help you identify skill gaps and chart your path to career success.</p>
          </div>
          <div className="features-grid">
            {features.map((f, i) => (
              <div key={i} className="feature-card">
                <div className="feature-icon">{f.icon}</div>
                <h3>{f.title}</h3>
                <p>{f.desc}</p>
              </div>
            ))}
          </div>
        </div>
      </section>

      {/* How It Works Section */}
      <section className="how-section" id="how-it-works">
        <div className="section-container">
          <div className="section-header">
            <span className="section-tag">How It Works</span>
            <h2>Four Simple Steps to <span className="gradient-text">Your Goal</span></h2>
            <p>Our streamlined process makes it easy to identify and close your skill gaps.</p>
          </div>
          <div className="steps-grid">
            {steps.map((s, i) => (
              <div key={i} className="step-card">
                <div className="step-num">{s.num}</div>
                <h3>{s.title}</h3>
                <p>{s.desc}</p>
                {i < steps.length - 1 && <div className="step-connector"></div>}
              </div>
            ))}
          </div>
        </div>
      </section>

      {/* Roles Preview Section */}
      <section className="roles-section" id="roles">
        <div className="section-container">
          <div className="section-header">
            <span className="section-tag">Explore Roles</span>
            <h2>In-Demand <span className="gradient-text">Tech Roles</span></h2>
            <p>Analyze your readiness for today's most sought-after technology positions.</p>
          </div>
          <div className="roles-grid">
            {[
              { title: 'Full Stack Developer', skills: ['JavaScript', 'React', 'Node.js', 'PostgreSQL', 'Docker'], color: '#6366f1' },
              { title: 'Backend Developer', skills: ['Java', 'Spring Boot', 'REST API', 'MySQL', 'Docker'], color: '#8b5cf6' },
              { title: 'Frontend Developer', skills: ['JavaScript', 'TypeScript', 'React', 'HTML/CSS', 'Git'], color: '#a855f7' },
              { title: 'Data Scientist', skills: ['Python', 'Machine Learning', 'TensorFlow', 'Pandas', 'Data Analysis'], color: '#ec4899' },
              { title: 'DevOps Engineer', skills: ['Docker', 'Kubernetes', 'AWS', 'CI/CD', 'Python'], color: '#f43f5e' },
            ].map((role, i) => (
              <div key={i} className="role-preview-card" style={{ '--accent': role.color }}>
                <h3>{role.title}</h3>
                <div className="role-skills-tags">
                  {role.skills.map((s, j) => (
                    <span key={j} className="role-skill-tag">{s}</span>
                  ))}
                </div>
                <Link to="/register" className="role-cta">Analyze My Skills →</Link>
              </div>
            ))}
          </div>
        </div>
      </section>

      {/* CTA Section */}
      <section className="cta-section">
        <div className="section-container">
          <div className="cta-card">
            <h2>Ready to Discover Your <span className="gradient-text">Potential</span>?</h2>
            <p>Join now and get a comprehensive analysis of your skills against top industry roles — completely free.</p>
            <Link to="/register" className="btn-hero btn-hero-primary">
              Create Free Account →
            </Link>
          </div>
        </div>
      </section>

      {/* Footer */}
      <footer className="landing-footer">
        <div className="section-container">
          <div className="footer-grid">
            <div className="footer-brand">
              <h3><span className="logo-icon">🎯</span> SkillGap Analyzer</h3>
              <p>Adaptive Skill Gap Analysis & Learning Recommendation System</p>
            </div>
            <div className="footer-links">
              <h4>Product</h4>
              <a href="#features">Features</a>
              <a href="#how-it-works">How It Works</a>
              <a href="#roles">Roles</a>
            </div>
            <div className="footer-links">
              <h4>Account</h4>
              <Link to="/login">Sign In</Link>
              <Link to="/register">Sign Up</Link>
            </div>
            <div className="footer-links">
              <h4>Team</h4>
              <span>Deepraj Thakur</span>
              <span>Alok Barekar</span>
              <span>Roshan Ahirwar</span>
            </div>
          </div>
          <div className="footer-bottom">
            <p>© 2026 SkillGap Analyzer — Minor Project, Jabalpur Engineering College</p>
          </div>
        </div>
      </footer>
    </div>
  );
};

export default LandingPage;

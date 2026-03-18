import React, { useState, useEffect } from 'react';
import { authService } from '../services/authService';
import { skillService } from '../services/skillService';
import './SkillProfile.css';

const proficiencyLabels = { 1: 'Beginner', 2: 'Intermediate', 3: 'Advanced', 4: 'Expert' };
const getProfLabel = (level) => proficiencyLabels[level] || proficiencyLabels[Math.min(level, 4)];

const SkillProfile = () => {
  const user = authService.getCurrentUser();
  const [userSkills, setUserSkills] = useState([]);
  const [allSkills, setAllSkills] = useState([]);
  const [loading, setLoading] = useState(true);
  const [showAddForm, setShowAddForm] = useState(false);
  const [selectedSkill, setSelectedSkill] = useState('');
  const [proficiencyLevel, setProficiencyLevel] = useState(2);

  useEffect(() => {
    loadData();
  }, []);

  const loadData = async () => {
    try {
      const [userSkillsData, allSkillsData] = await Promise.all([
        skillService.getUserSkills(user.userId),
        skillService.getAllSkills(),
      ]);
      setUserSkills(userSkillsData);
      setAllSkills(allSkillsData);
    } catch (error) {
      console.error('Error loading skills:', error);
    } finally {
      setLoading(false);
    }
  };

  const handleAddSkill = async (e) => {
    e.preventDefault();
    try {
      await skillService.addUserSkill(user.userId, {
        skillId: parseInt(selectedSkill),
        proficiencyLevel: proficiencyLevel,
      });
      await loadData();
      setShowAddForm(false);
      setSelectedSkill('');
      setProficiencyLevel(2);
    } catch (error) {
      alert(error.response?.data?.error || 'Failed to add skill');
    }
  };

  const handleDeleteSkill = async (skillId) => {
    if (confirm('Are you sure you want to remove this skill?')) {
      try {
        await skillService.deleteUserSkill(user.userId, skillId);
        await loadData();
      } catch (error) {
        alert('Failed to delete skill');
      }
    }
  };

  const availableSkills = allSkills.filter(
    (skill) => !userSkills.some((us) => us.skillId === skill.id)
  );

  const groupedSkills = userSkills.reduce((acc, skill) => {
    const category = skill.skillCategory || 'Other';
    if (!acc[category]) acc[category] = [];
    acc[category].push(skill);
    return acc;
  }, {});

  if (loading) {
    return <div className="container"><div className="loading">Loading skills...</div></div>;
  }

  return (
    <div className="container skill-profile fade-in">
      <div className="page-header">
        <h1>My Skill Profile</h1>
        <button
          className="btn btn-primary"
          onClick={() => setShowAddForm(!showAddForm)}
        >
          {showAddForm ? '✕ Cancel' : '➕ Add Skill'}
        </button>
      </div>

      {showAddForm && (
        <div className="add-skill-form card">
          <h2>Add New Skill</h2>
          <form onSubmit={handleAddSkill}>
            <div className="input-group">
              <label>Select Skill</label>
              <select
                value={selectedSkill}
                onChange={(e) => setSelectedSkill(e.target.value)}
                required
              >
                <option value="">Choose a skill...</option>
                {availableSkills.map((skill) => (
                  <option key={skill.id} value={skill.id}>
                    {skill.name} ({skill.category})
                  </option>
                ))}
              </select>
            </div>

            <div className="input-group">
              <label>Proficiency Level</label>
              <select
                value={proficiencyLevel}
                onChange={(e) => setProficiencyLevel(parseInt(e.target.value))}
              >
                <option value={1}>Beginner</option>
                <option value={2}>Intermediate</option>
                <option value={3}>Advanced</option>
                <option value={4}>Expert</option>
              </select>
            </div>

            <button type="submit" className="btn btn-primary">
              Add Skill
            </button>
          </form>
        </div>
      )}

      <div className="skills-by-category">
        {Object.keys(groupedSkills).length === 0 ? (
          <div className="empty-state card">
            <h2>No skills added yet</h2>
            <p>Start building your profile by adding your first skill!</p>
          </div>
        ) : (
          Object.entries(groupedSkills).map(([category, skills]) => (
            <div key={category} className="category-section card">
              <h2>{category}</h2>
              <div className="skills-grid">
                {skills.map((skill) => (
                  <div key={skill.id} className="skill-card">
                    <div className="skill-header">
                      <h3>{skill.skillName}</h3>
                      <button
                        className="delete-btn"
                        onClick={() => handleDeleteSkill(skill.skillId)}
                      >
                        ✕
                      </button>
                    </div>
                    <div className="skill-level-display">
                      <div className="level-dots">
                        {[1, 2, 3, 4].map((level) => (
                          <span
                            key={level}
                            className={`dot ${
                              level <= Math.min(skill.proficiencyLevel, 4) ? 'active' : ''
                            }`}
                          />
                        ))}
                      </div>
                      <span className="level-text">
                        {getProfLabel(skill.proficiencyLevel)}
                      </span>
                    </div>
                  </div>
                ))}
              </div>
            </div>
          ))
        )}
      </div>
    </div>
  );
};

export default SkillProfile;

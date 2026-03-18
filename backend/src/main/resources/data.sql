-- Initial data for Skills
INSERT INTO skills (name, category, description) VALUES
('Java', 'Programming', 'Object-oriented programming language'),
('Python', 'Programming', 'High-level programming language'),
('JavaScript', 'Programming', 'Web programming language'),
('TypeScript', 'Programming', 'Typed superset of JavaScript'),
('React', 'Frontend', 'JavaScript library for building UIs'),
('Angular', 'Frontend', 'TypeScript-based web framework'),
('Vue.js', 'Frontend', 'Progressive JavaScript framework'),
('HTML/CSS', 'Frontend', 'Web markup and styling'),
('Spring Boot', 'Backend', 'Java framework for building applications'),
('Node.js', 'Backend', 'JavaScript runtime for server-side'),
('Express.js', 'Backend', 'Web framework for Node.js'),
('Django', 'Backend', 'Python web framework'),
('Flask', 'Backend', 'Lightweight Python web framework'),
('MySQL', 'Database', 'Relational database management system'),
('PostgreSQL', 'Database', 'Advanced relational database'),
('MongoDB', 'Database', 'NoSQL document database'),
('Redis', 'Database', 'In-memory data structure store'),
('Git', 'DevOps', 'Version control system'),
('Docker', 'DevOps', 'Containerization platform'),
('Kubernetes', 'DevOps', 'Container orchestration'),
('AWS', 'Cloud', 'Amazon Web Services cloud platform'),
('Azure', 'Cloud', 'Microsoft cloud platform'),
('GCP', 'Cloud', 'Google Cloud Platform'),
('CI/CD', 'DevOps', 'Continuous Integration/Deployment'),
('REST API', 'Backend', 'RESTful API design'),
('GraphQL', 'Backend', 'Query language for APIs'),
('Machine Learning', 'Data Science', 'ML algorithms and models'),
('Data Analysis', 'Data Science', 'Data processing and analysis'),
('TensorFlow', 'Data Science', 'ML framework'),
('Pandas', 'Data Science', 'Data manipulation library');

-- Initial data for Job Roles
INSERT INTO job_roles (name, description) VALUES
('Full Stack Developer', 'Develops both frontend and backend of web applications'),
('Backend Developer', 'Focuses on server-side logic and database management'),
('Frontend Developer', 'Specializes in user interface development'),
('Data Scientist', 'Analyzes data and builds ML models'),
('DevOps Engineer', 'Manages infrastructure and deployment pipelines');

-- Role Skill Requirements for Full Stack Developer
INSERT INTO role_skill_requirements (role_id, skill_id, required_proficiency_level) VALUES
(1, (SELECT id FROM skills WHERE name = 'JavaScript'), 4),
(1, (SELECT id FROM skills WHERE name = 'React'), 4),
(1, (SELECT id FROM skills WHERE name = 'HTML/CSS'), 4),
(1, (SELECT id FROM skills WHERE name = 'Node.js'), 3),
(1, (SELECT id FROM skills WHERE name = 'Express.js'), 3),
(1, (SELECT id FROM skills WHERE name = 'MySQL'), 3),
(1, (SELECT id FROM skills WHERE name = 'PostgreSQL'), 3),
(1, (SELECT id FROM skills WHERE name = 'Git'), 4),
(1, (SELECT id FROM skills WHERE name = 'REST API'), 4),
(1, (SELECT id FROM skills WHERE name = 'Docker'), 2);

-- Role Skill Requirements for Backend Developer
INSERT INTO role_skill_requirements (role_id, skill_id, required_proficiency_level) VALUES
(2, (SELECT id FROM skills WHERE name = 'Java'), 4),
(2, (SELECT id FROM skills WHERE name = 'Spring Boot'), 4),
(2, (SELECT id FROM skills WHERE name = 'MySQL'), 4),
(2, (SELECT id FROM skills WHERE name = 'PostgreSQL'), 3),
(2, (SELECT id FROM skills WHERE name = 'REST API'), 4),
(2, (SELECT id FROM skills WHERE name = 'Git'), 4),
(2, (SELECT id FROM skills WHERE name = 'Docker'), 3),
(2, (SELECT id FROM skills WHERE name = 'MongoDB'), 2);

-- Role Skill Requirements for Frontend Developer
INSERT INTO role_skill_requirements (role_id, skill_id, required_proficiency_level) VALUES
(3, (SELECT id FROM skills WHERE name = 'JavaScript'), 4),
(3, (SELECT id FROM skills WHERE name = 'TypeScript'), 3),
(3, (SELECT id FROM skills WHERE name = 'React'), 4),
(3, (SELECT id FROM skills WHERE name = 'HTML/CSS'), 5),
(3, (SELECT id FROM skills WHERE name = 'Git'), 4),
(3, (SELECT id FROM skills WHERE name = 'REST API'), 3);

-- Role Skill Requirements for Data Scientist
INSERT INTO role_skill_requirements (role_id, skill_id, required_proficiency_level) VALUES
(4, (SELECT id FROM skills WHERE name = 'Python'), 5),
(4, (SELECT id FROM skills WHERE name = 'Machine Learning'), 4),
(4, (SELECT id FROM skills WHERE name = 'Data Analysis'), 5),
(4, (SELECT id FROM skills WHERE name = 'TensorFlow'), 3),
(4, (SELECT id FROM skills WHERE name = 'Pandas'), 4),
(4, (SELECT id FROM skills WHERE name = 'PostgreSQL'), 4);

-- Role Skill Requirements for DevOps Engineer
INSERT INTO role_skill_requirements (role_id, skill_id, required_proficiency_level) VALUES
(5, (SELECT id FROM skills WHERE name = 'Git'), 5),
(5, (SELECT id FROM skills WHERE name = 'Docker'), 5),
(5, (SELECT id FROM skills WHERE name = 'Kubernetes'), 4),
(5, (SELECT id FROM skills WHERE name = 'AWS'), 4),
(5, (SELECT id FROM skills WHERE name = 'CI/CD'), 4),
(5, (SELECT id FROM skills WHERE name = 'Python'), 3);

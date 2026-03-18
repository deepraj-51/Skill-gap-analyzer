import api from './api';

export const aiService = {
    generateLearningPath: async (userId, roleId) => {
        const response = await api.post('/learning-path/generate', { userId, roleId });
        return response.data;
    },
};

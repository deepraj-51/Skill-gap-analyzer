import api from './api';

export const skillService = {
    getAllSkills: async () => {
        const response = await api.get('/skills');
        return response.data;
    },

    getUserSkills: async (userId) => {
        const response = await api.get(`/user-skills/${userId}`);
        return response.data;
    },

    addUserSkill: async (userId, skillData) => {
        const response = await api.post(`/user-skills/${userId}`, skillData);
        return response.data;
    },

    updateUserSkill: async (userId, skillId, proficiencyLevel) => {
        const response = await api.put(`/user-skills/${userId}/skills/${skillId}`, {
            proficiencyLevel,
        });
        return response.data;
    },

    deleteUserSkill: async (userId, skillId) => {
        const response = await api.delete(`/user-skills/${userId}/skills/${skillId}`);
        return response.data;
    },
};

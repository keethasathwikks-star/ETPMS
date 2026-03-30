const axios = require('axios');

async function testPost() {
    try {
        const response = await axios.post('http://localhost:8080/api/employees', {
            name: 'Test Tester',
            email: 'test@example.com',
            department: 'Engineering',
            role: 'Dev'
        });
        console.log('SUCCESS:', response.status);
        console.log(response.data);
    } catch (error) {
        if (error.response) {
            console.log('ERROR STATUS:', error.response.status);
            console.log('ERROR DATA:', error.response.data);
        } else {
            console.log('ERROR MESSAGE:', error.message);
        }
    }
}

testPost();

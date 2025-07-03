import axios from 'axios';

async function testLogin() {
  try {
    const res = await axios.post('http://localhost:8080/api/users/login', {
      username: 'testUser',
      password: 'testPass',
    });
    console.log('Token:', res.data);
  } catch (err: any) {
    if (axios.isAxiosError(err)) {
      console.log('Login hata:', err.response?.data || err.message);
    } else {
      console.log('Bilinmeyen hata:', err.message || err);
    }
  }
}

testLogin();

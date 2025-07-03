import React, { useState } from 'react';
import { loginUser } from '../services/authService'; 

const Login: React.FC = () => {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!username || !password) {
      alert('Kanka alanları boş bırakma!');
      return;
    }
    try {
      const token = await loginUser({ username, password });
      localStorage.setItem('token', token); 
      alert('Giriş başarılı!');
      window.location.href = '/accounts'; 
    } catch (err: any) {
      console.error(err);
      if (err.response?.status === 401) {
        alert('Kullanıcı adı ya da şifre yanlış');
      } else {
        alert('Sunucu hatası, sonra dene');
      }
    }
  };

  return (
    <div style={{ padding: '20px', maxWidth: '320px', margin: 'auto' }}>
      <h2>Giriş Yap</h2>
      <form onSubmit={handleSubmit}>
        <input
          type="text"
          placeholder="Kullanıcı Adı"
          value={username}
          onChange={e => setUsername(e.target.value)}
          required
          style={{ width: '100%', marginBottom: '10px', padding: '8px' }}
        />
        <input
          type="password"
          placeholder="Şifre"
          value={password}
          onChange={e => setPassword(e.target.value)}
          required
          style={{ width: '100%', marginBottom: '10px', padding: '8px' }}
        />
        <button
          type="submit"
          style={{
            width: '100%',
            padding: '10px',
            backgroundColor: '#007bff',
            color: 'white',
            border: 'none',
            borderRadius: '4px'
          }}
        >
          Giriş Yap
        </button>
      </form>
    </div>
  );
};

export default Login;

import React, { useState } from 'react';
import { Link } from 'react-router-dom';
import { registerUser } from '../services/authService';

const Register: React.FC = () => {
  const [username, setUsername] = useState('');
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!username || !email || !password) {
      alert('Hepsini doldur kanka!');
      return;
    }
    try {
      await registerUser({ username, email, password });
      alert('Kayıt başarılı, giriş sayfasına yönlendiriliyorsun');
      window.location.href = '/login';
    } catch (err: any) {
      console.error(err);
      alert('Kayıt hatası, tekrar dene');
    }
  };

  return (
    <div style={{ padding: '20px', maxWidth: '320px', margin: 'auto' }}>
      <h2>Kayıt Ol</h2>
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
          type="email"
          placeholder="E‑posta"
          value={email}
          onChange={e => setEmail(e.target.value)}
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
        <div style={{ display: 'flex', gap: '10px' }}>
          <button
            type="submit"
            style={{
              flex: 1,
              padding: '10px',
              backgroundColor: '#28a745',
              color: 'white',
              border: 'none',
              borderRadius: '4px'
            }}
          >
            Kayıt Ol
          </button>
          <Link
            to="/login"
            style={{
              flex: 1,
              display: 'inline-block',
              textAlign: 'center',
              padding: '10px',
              backgroundColor: '#007bff',
              color: 'white',
              textDecoration: 'none',
              borderRadius: '4px'
            }}
          >
            Girişe Dön
          </Link>
        </div>
      </form>
    </div>
  );
};

export default Register;

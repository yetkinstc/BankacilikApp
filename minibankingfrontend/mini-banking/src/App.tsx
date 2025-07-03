import React from 'react';
import { BrowserRouter, Routes, Route, Navigate, Link } from 'react-router-dom';
import Login from './pages/Login';
import Register from './pages/Register';
import Accounts from './pages/Account';
import Transactions from './pages/Transactions';
function App() {
  return (
    <BrowserRouter>
      <nav style={{ padding: '10px', textAlign: 'center' }}>
        <Link to="/login" style={{ marginRight: '10px' }}>Giriş</Link>
        <Link to="/register">Kayıt</Link>
      </nav>
      <Routes>
        <Route path="/" element={<Navigate to="/login" replace />} />
        <Route path="/login" element={<Login />} />
        <Route path="/register" element={<Register />} />
        <Route path="/accounts" element={<Accounts />} />
        <Route path="/transactions" element={<Transactions />} />
      </Routes>
    </BrowserRouter>
  );
}

export default App;

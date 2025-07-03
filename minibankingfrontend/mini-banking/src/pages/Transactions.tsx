import React, { useState } from 'react';
import axios from 'axios';
//@ts-ignore
import { useNavigate } from 'react-router-dom'; 

const isUUID = (str: string) => {
  const uuidRegex = /^[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}$/i;
  return uuidRegex.test(str);
};

const TransferForm: React.FC = () => {
  const [fromAccountId, setFromAccountId] = useState('');
  const [toAccountId, setToAccountId] = useState('');
  const [amount, setAmount] = useState('');
  const [message, setMessage] = useState('');
  const [error, setError] = useState('');
  const navigate = useNavigate(); 

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setMessage('');
    setError('');

    if (!isUUID(fromAccountId)) {
      setError('Gönderen hesap ID geçersiz UUID formatında olmalı.');
      return;
    }
    if (!isUUID(toAccountId)) {
      setError('Alıcı hesap ID geçersiz UUID formatında olmalı.');
      return;
    }

    const numAmount = Number(amount);
    if (isNaN(numAmount) || numAmount <= 0) {
      setError('Miktar sıfırdan büyük bir sayı olmalı.');
      return;
    }

    try {
      const res = await axios.post('/api/transactions/transfer', null, {
        params: {
          fromAccountId,
          toAccountId,
          amount: numAmount,
        }
      });
      setMessage('Transfer başarılı! Transaction ID: ' + res.data.id);
      setFromAccountId('');
      setToAccountId('');
      setAmount('');
    } catch (error: any) {
      setError('Transfer başarısız: ' + (error.response?.data?.message || error.message));
    }
  };

  return (
    <form onSubmit={handleSubmit} style={{ marginBottom: 20 }}>
      <h2>Para Transferi</h2>

      <input
        type="text"
        placeholder="Gönderen Hesap ID (UUID formatında)"
        value={fromAccountId}
        onChange={e => setFromAccountId(e.target.value)}
        required
        style={{ marginRight: 8, padding: 8, width: '300px' }}
      />

      <input
        type="text"
        placeholder="Alıcı Hesap ID (UUID formatında)"
        value={toAccountId}
        onChange={e => setToAccountId(e.target.value)}
        required
        style={{ marginRight: 8, padding: 8, width: '300px' }}
      />

      <input
        type="number"
        placeholder="Miktar"
        value={amount}
        onChange={e => setAmount(e.target.value)}
        required
        min={0.01}
        step={0.01}
        style={{ marginRight: 8, padding: 8, width: '150px' }}
      />

      <button type="submit" style={{ padding: '8px 16px' }}>
        Gönder
      </button>

      {/* GERİ DÖN BUTONU */}
      <button
        type="button"
        onClick={() => navigate('/accounts')}
        style={{ padding: '8px 16px', marginLeft: 10 }}
      >
        Hesaplara Geri Dön
      </button>

      {message && <p style={{ color: 'green', marginTop: 10 }}>{message}</p>}
      {error && <p style={{ color: 'red', marginTop: 10 }}>{error}</p>}
    </form>
  );
};

export default TransferForm;

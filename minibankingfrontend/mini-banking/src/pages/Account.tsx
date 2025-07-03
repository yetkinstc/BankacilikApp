import React, { useEffect, useState } from 'react';
import axios from 'axios';
//@ts-ignore
import { useNavigate } from 'react-router-dom';

axios.defaults.baseURL = 'http://localhost:8080';

axios.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('token');
    if (token && config.headers) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => Promise.reject(error)
);

interface Account {
  id: string;
  name: string;
  number: string;
  balance: number;
}

const AccountPage: React.FC = () => {
  const navigate = useNavigate();

  const [accounts, setAccounts] = useState<Account[]>([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  // Form state
  const [name, setName] = useState('');
  const [number, setNumber] = useState('');
  const [balance, setBalance] = useState<number>(0);

  // Arama filtreleri
  const [filterNumber, setFilterNumber] = useState('');
  const [filterName, setFilterName] = useState('');

  // Düzenleme için seçili id
  const [editingId, setEditingId] = useState<string | null>(null);

  // Detay gösterimi için seçili hesap
  const [selectedAccount, setSelectedAccount] = useState<Account | null>(null);

  // Hesapları filtreli çek
  const fetchAccounts = async () => {
    setLoading(true);
    setError(null);
    try {
      const params: any = {};
      if (filterNumber) params.number = filterNumber;
      if (filterName) params.name = filterName;

      const res = await axios.get<Account[]>('/api/accounts/search', { params });
      setAccounts(res.data);
    } catch {
      setError('Hesaplar yüklenirken hata oluştu');
    }
    setLoading(false);
  };

  useEffect(() => {
    if (!localStorage.getItem('token')) {
      setError('Giriş yapılmamış');
      setLoading(false);
    } else {
      fetchAccounts();
    }
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  // Formu sıfırla
  const resetForm = () => {
    setName('');
    setNumber('');
    setBalance(0);
    setEditingId(null);
  };

  // Form submit
  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();

    try {
      if (editingId) {
        // Güncelle
        const res = await axios.put<Account>(`/api/accounts/${editingId}`, {
          name,
          number,
          balance,
        });
        setAccounts(accounts.map((acc) => (acc.id === editingId ? res.data : acc)));
        resetForm();
      } else {
        // Yeni ekle
        const res = await axios.post<Account>('/api/accounts/create', { name, number, balance });
        setAccounts([...accounts, res.data]);
        resetForm();
      }
    } catch {
      alert(editingId ? 'Güncelleme başarısız' : 'Hesap oluşturma başarısız');
    }
  };

  // Hesap sil
  const handleDelete = async (id: string) => {
    if (!window.confirm('Bu hesabı silmek istediğine emin misin?')) return;
    try {
      await axios.delete(`/api/accounts/${id}`);
      setAccounts(accounts.filter((acc) => acc.id !== id));
      if (selectedAccount?.id === id) setSelectedAccount(null);
    } catch {
      alert('Silme işlemi başarısız');
    }
  };

  // Hesap düzenle
  const handleEdit = (acc: Account) => {
    setName(acc.name);
    setNumber(acc.number);
    setBalance(acc.balance);
    setEditingId(acc.id);
    setSelectedAccount(null);
  };

  // Detay göster
  const handleView = async (id: string) => {
    setError(null);
    try {
      const res = await axios.get<Account>(`/api/accounts/${id}`);
      setSelectedAccount(res.data);
      resetForm();
      setEditingId(null);
    } catch {
      setError('Detaylar yüklenirken hata oluştu');
    }
  };

  // Arama butonu
  const handleSearch = () => {
    fetchAccounts();
    setSelectedAccount(null);
    resetForm();
  };

  if (loading) return <div>Yükleniyor...</div>;
  if (error) return <div style={{ color: 'red' }}>{error}</div>;

  return (
    <div style={{ maxWidth: 600, margin: 'auto' }}>
      <h1>Hesaplarım</h1>

      {/* Transactions Butonu */}
      <button
        onClick={() => navigate('/transactions')}
        style={{
          marginBottom: 20,
          padding: '10px 20px',
          backgroundColor: '#4caf50',
          color: 'white',
          border: 'none',
          borderRadius: 4,
          cursor: 'pointer',
        }}
      >
        Transactions'a Git
      </button>

      {/* Arama */}
      <div style={{ marginBottom: 20 }}>
        <input
          type="text"
          placeholder="Numara ile ara"
          value={filterNumber}
          onChange={(e) => setFilterNumber(e.target.value)}
          style={{ marginRight: 8, padding: 8 }}
        />
        <input
          type="text"
          placeholder="İsim ile ara"
          value={filterName}
          onChange={(e) => setFilterName(e.target.value)}
          style={{ marginRight: 8, padding: 8 }}
        />
        <button onClick={handleSearch} style={{ padding: '8px 16px' }}>
          Ara
        </button>
      </div>

      {/* Ekleme / Güncelleme Formu */}
      <form onSubmit={handleSubmit} style={{ marginBottom: 20 }}>
        <input
          type="text"
          placeholder="Hesap Adı"
          value={name}
          onChange={(e) => setName(e.target.value)}
          required
          style={{ marginRight: 8, padding: 8 }}
        />
        <input
          type="text"
          placeholder="Hesap Numarası"
          value={number}
          onChange={(e) => setNumber(e.target.value)}
          required
          style={{ marginRight: 8, padding: 8 }}
        />
        <input
          type="number"
          placeholder="Bakiye"
          value={balance}
          onChange={(e) => setBalance(Number(e.target.value))}
          required
          style={{ marginRight: 8, padding: 8, width: 100 }}
          min={0}
          step={0.01}
        />
        <button type="submit" style={{ padding: '8px 16px' }}>
          {editingId ? 'Güncelle' : 'Ekle'}
        </button>
        {editingId && (
          <button
            type="button"
            onClick={resetForm}
            style={{ marginLeft: 8, padding: '8px 16px', backgroundColor: '#888', color: 'white', border: 'none' }}
          >
            İptal
          </button>
        )}
      </form>

      {/* Hesap listesi */}
      <ul style={{ listStyle: 'none', padding: 0 }}>
        {accounts.map((acc) => (
          <li
            key={acc.id}
            style={{
              border: '1px solid #ddd',
              padding: 12,
              marginBottom: 8,
              display: 'flex',
              flexDirection: 'column',
              alignItems: 'flex-start',
            }}
          >
            <div>
              <b>{acc.name}</b> — {acc.number} — Bakiye: {acc.balance.toFixed(2)} ₺
            </div>
            <div style={{ fontSize: 12, color: '#666', marginBottom: 8 }}>
              ID: {acc.id}
            </div>
            <div>
              <button onClick={() => handleEdit(acc)} style={{ marginRight: 8, padding: '4px 8px' }}>
                Düzenle
              </button>
              <button onClick={() => handleView(acc.id)} style={{ marginRight: 8, padding: '4px 8px' }}>
                Detay
              </button>
              <button
                onClick={() => handleDelete(acc.id)}
                style={{ padding: '4px 8px', backgroundColor: '#f44336', color: 'white', border: 'none' }}
              >
                Sil
              </button>
            </div>
          </li>
        ))}
      </ul>

      {/* Detay gösterimi */}
      {selectedAccount && (
        <div style={{ border: '1px solid #aaa', padding: 16, marginTop: 20 }}>
          <h2>Hesap Detayları</h2>
          <p>
            <b>İsim:</b> {selectedAccount.name}
          </p>
          <p>
            <b>Numara:</b> {selectedAccount.number}
          </p>
          <p>
            <b>Bakiye:</b> {selectedAccount.balance.toFixed(2)} ₺
          </p>
          <button onClick={() => setSelectedAccount(null)}>Kapat</button>
        </div>
      )}
    </div>
  );
};

export default AccountPage;

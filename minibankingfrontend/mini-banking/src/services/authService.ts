import axios from 'axios';

axios.defaults.baseURL = 'http://localhost:8080'; // backend’in adresi

export interface LoginRequest {
  username: string;
  password: string;
}

export async function loginUser(req: LoginRequest): Promise<string> {
  const response = await axios.post<string>('/api/users/login', req);
  return response.data; // bu JWT token
}

export interface RegisterRequest {
  username: string;
  email: string;
  password: string;
}

export async function registerUser(req: RegisterRequest): Promise<void> {
  await axios.post('/api/users/register', req);
}

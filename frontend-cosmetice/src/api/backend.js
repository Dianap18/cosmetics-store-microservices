import axios from "axios";

const GATEWAY = "http://localhost:8080";

export const PRODUCT_API   = `${GATEWAY}/api/produse`;
export const INVENTORY_API = `${GATEWAY}/api/inventory`;
export const USER_API      = `${GATEWAY}/api/users`;
export const REPORT_API    = `${GATEWAY}/api/rapoarte`;

export const apiService = {
  getProducts: () => axios.get(PRODUCT_API),
  addProduct: (data) => axios.post(PRODUCT_API, data),
  updateProduct: (id, data) => axios.put(`${PRODUCT_API}/${id}`, data),
  deleteProduct: (id) => axios.delete(`${PRODUCT_API}/${id}`),
  searchClient: (denumire) => axios.get(`${PRODUCT_API}/cautare-client`, { params: { denumire } }),
  searchAngajat: (denumire, idMagazin) => axios.get(`${PRODUCT_API}/cautare-angajat`, { params: { denumire, idMagazin } }),
  
  getProductStockDirect: (idProdus, idMagazin) => axios.get(`${INVENTORY_API}/stoc`, { params: { idProdus, idMagazin } }),
  getMagazineCuStoc: (idProdus) => axios.get(`${INVENTORY_API}/magazine-cu-stoc`, { params: { idProdus } }),
  sellProduct: (idMagazin, idProdus, cantitate) => axios.post(`${INVENTORY_API}/vanzare`, { idMagazin, idProdus, cantitate }),
  updateStock: (idMagazin, idProdus, cantitateNoua) => axios.put(`${INVENTORY_API}/actualizare`, { idMagazin, idProdus, cantitateNoua: cantitateNoua }),
  getAllStores: () => axios.get(`${INVENTORY_API}/magazine`),

  getStats: () => axios.get(`${REPORT_API}/statistici`),
  exportReport: (format) => axios.get(`${REPORT_API}/export/${format}`, { responseType: 'blob' }),

  login: (email, password) => axios.post(`${USER_API}/login`, { username: email, parola: password }),
  getUsers: () => axios.get(USER_API),
  addUser: (data) => axios.post(USER_API, data),
  updateUser: (id, data) => axios.put(`${USER_API}/${id}`, data),
  deleteUser: (id) => axios.delete(`${USER_API}/${id}`),
  exportUsers: () => axios.get(`${USER_API}/export/csv`, { responseType: 'blob' }),
};
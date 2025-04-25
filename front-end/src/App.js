import React from 'react';

import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';

import AllRoomsPage from './component/booking_rooms/AllRoomsPage';
import Footer from './component/common/Footer';
import Home from './component/home/Home';
import Navbar from './component/common/Navbar';

import './App.css';

function App() {
  return (
    <BrowserRouter>
      <div className="App">
        <Navbar />

        <div className="content">
          <Routes>
            <Route exact path="/" element={<Home />} />
            <Route exact path="/rooms" element={<AllRoomsPage />} />
          </Routes>
        </div>

        <Footer />
      </div>
    </BrowserRouter>
  );
}

export default App;
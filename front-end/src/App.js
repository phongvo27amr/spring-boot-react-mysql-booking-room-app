import React from 'react';

import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';

import AllRoomsPage from './component/booking_rooms/AllRoomsPage';
import FindBookingPage from './component/booking_rooms/FindBookingPage';
import Footer from './component/common/Footer';
import Home from './component/home/Home';
import LoginPage from './component/auth/LoginPage';
import Navbar from './component/common/Navbar';
import RegisterPage from './component/auth/RegisterPage';
import RoomDetailsPage from './component/booking_rooms/RoomDetailsPage';

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
            <Route path="/find-booking" element={<FindBookingPage />} />
            <Route path="/room-details-book/:roomId" element={<RoomDetailsPage />} />
            <Route path="/login" element={<LoginPage />} />
            <Route path="/register" element={<RegisterPage />} />
          </Routes>
        </div>

        <Footer />
      </div>
    </BrowserRouter>
  );
}

export default App;
import React from 'react';

import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';

import Footer from './component/common/Footer';
import Home from './component/home/Home';
import Navbar from './component/common/Navbar';

import AllRoomsPage from './component/booking_rooms/AllRoomsPage';
import LoginPage from './component/auth/LoginPage';
import RegisterPage from './component/auth/RegisterPage';
import FindBookingPage from './component/booking_rooms/FindBookingPage';

import ProfilePage from './component/profile/ProfilePage';
import EditProfilePage from './component/profile/EditProfilePage';
import RoomDetailsPage from './component/booking_rooms/RoomDetailsPage';

import AddRoomPage from './component/admin/AddRoomPage';
import AdminPage from './component/admin/AdminPage';
import EditBookingPage from './component/admin/EditBookingPage';
import EditRoomPage from './component/admin/EditRoomPage';
import ManageBookingsPage from './component/admin/ManageBookingsPage';
import ManageRoomsPage from './component/admin/ManageRoomsPage';

import { ProtectedRoute, AdminRoute } from './service/guard';

import './App.css';

function App() {
  return (
    <BrowserRouter>
      <div className="App">
        <Navbar />

        <div className="content">
          <Routes>
            {/* Public routes */}
            <Route exact path="/" element={<Home />} />
            <Route exact path="*" element={<Navigate to="/" />} />
            <Route exact path="/rooms" element={<AllRoomsPage />} />
            <Route path="/find-booking" element={<FindBookingPage />} />
            <Route path="/login" element={<LoginPage />} />
            <Route path="/register" element={<RegisterPage />} />

            {/* Authenticated user routes */}
            <Route path="/room-details-book/:roomId" element={<ProtectedRoute element={<RoomDetailsPage />} />} />
            <Route path="/profile" element={<ProtectedRoute element={<ProfilePage />} />} />
            <Route path="/edit-profile" element={<ProtectedRoute element={<EditProfilePage />} />} />

            {/* Admin routes */}
            <Route path="/admin" element={<AdminRoute element={<AdminPage />} />} />
            <Route path="/admin/manage-rooms" element={<AdminRoute element={<ManageRoomsPage />} />} />
            <Route path="/admin/manage-bookings" element={<AdminRoute element={<ManageBookingsPage />} />} />
            <Route path="/admin/add-room" element={<AdminRoute element={<AddRoomPage />} />} />
            <Route path="/admin/edit-room/:roomId" element={<AdminRoute element={<EditRoomPage />} />} />
            <Route path="/admin/edit-booking/:bookingCode" element={<AdminRoute element={<EditBookingPage />} />} />
          </Routes>
        </div>

        <Footer />
      </div>
    </BrowserRouter>
  );
}

export default App;
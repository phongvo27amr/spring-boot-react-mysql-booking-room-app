import React from 'react';

import { NavLink, useNavigate } from 'react-router-dom';

import ApiService from '../../service/ApiService';

function Navbar() {
  const isAuthenticated = ApiService.isAuthenticated();
  const isAdmin = ApiService.isAdmin();
  const isUser = ApiService.isUser();
  const navigate = useNavigate();

  const handleLogout = () => {
    const isLogout = window.confirm('Are you sure you really want to log out?');
    if (isLogout) {
      ApiService.logout();
      navigate('/');
    }
  }

  return (
    <nav className="navbar">
      <div className="navbar-brand">
        <NavLink to="/">Hotel Booking</NavLink>
      </div>

      <ul className="navbar-ul">
        <li><NavLink to="/" activeclass="active">Home</NavLink></li>
        <li><NavLink to="/rooms" activeclass="active">Rooms</NavLink></li>
        <li><NavLink to="/find-booking" activeclass="active">Find My Bookings</NavLink></li>

        {isUser && <li><NavLink to="/profile" activeclass="active">Profile</NavLink></li>}
        {isAdmin && <li><NavLink to="/admin" activeclass="active">Admin</NavLink></li>}

        {!isAuthenticated && <li><NavLink to="/login" activeclass="active">Log In</NavLink></li>}
        {!isAuthenticated && <li><NavLink to="/register" activeclass="active">Register</NavLink></li>}

        {isAuthenticated && <li onClick={handleLogout}>Log Out</li>}
      </ul>
    </nav>
  );
}

export default Navbar;
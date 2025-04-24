import React from 'react';

import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';

import Navbar from './component/common/Navbar';
import Footer from './component/common/Footer';
import Home from './component/home/Home';

import './App.css';

function App() {
  return (
    <BrowserRouter>
      <div className="App">
        <Navbar />

        <div className="content">
          <Routes>
            <Route exact path="/home" element={<Home />} />
          </Routes>
        </div>

        <Footer />
      </div>
    </BrowserRouter>
  );
}

export default App;
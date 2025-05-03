import React, { useState } from 'react';
import RoomSearch from '../common/RoomSearch';
import RoomResult from '../common/RoomResult';

const Home = () => {
  const [roomSearchResults, setRoomSearchResults] = useState([]);

  const handleSearchResult = (results) => {
    setRoomSearchResults(results);
  };

  return (
    <div className="home">
      <section>
        <header className="header-banner">
          <img src="./assets/images/banner.png" alt="Study Space Booking" className="header-image" />
          <div className="overlay"></div>
          <div className="animated-texts overlay-content">
            <h1>
              Welcome to <span className="phegon-color">Study Space Booking</span>
            </h1><br />

            <h3>Step into a haven of comfort and concentrate on your study</h3>
          </div>
        </header>
      </section>

      <RoomSearch handleSearchResult={handleSearchResult} />
      <RoomResult roomSearchResults={roomSearchResults} />

      <h2 className="home-services">Services at <span className="phegon-color">Our Space Booking</span></h2>

      <section className="service-section"><div className="service-card">
        <img src="./assets/images/icons8-air-conditioner-96.png" alt="Air Conditioning" width="50px" height="50px" />
        <div className="service-details">
          <h3 className="service-title">Air Conditioning</h3>
          <p className="service-description">Stay focused and comfortable in a climate-controlled environment. All study rooms are equipped with air conditioning to maintain a pleasant temperature, helping you concentrate better during long study sessions.</p>
        </div>
      </div>
        <div className="service-card">
          <img src="./assets/images/icons8-wifi-96.png" alt="Internet Access" width="50px" height="50px" />
          <div className="service-details">
            <h3 className="service-title">Internet Access</h3>
            <p className="service-description">Enjoy fast and reliable Wi-Fi in every room. Whether you're attending online classes, conducting research, or collaborating on group projects, our high-speed internet ensures you stay connected without interruption.</p>
          </div>
        </div>
        <div className="service-card">
          <img src="./assets/images/icons8-power-96.png" alt="Power Outlets" width="50px" height="50px" />
          <div className="service-details">
            <h3 className="service-title">Power Outlets</h3>
            <p className="service-description">Keep your devices fully charged throughout your booking. Each study room includes multiple easily accessible power outlets, so you never have to worry about running out of battery during important work.</p>
          </div>
        </div>
        <div className="service-card">
          <img src="./assets/images/icons8-presentation-96.png" alt="Whiteboard/Presentation Screen" width="50px" height="50px" />
          <div className="service-details">
            <h3 className="service-title">Whiteboard/Presentation Screen</h3>
            <p className="service-description">Enhance your study sessions with visual tools. Our rooms feature whiteboards or presentation screens, perfect for brainstorming ideas, working through problems, or delivering group presentations.</p>
          </div>
        </div>

      </section>

      <section></section>
    </div>
  );
}

export default Home;
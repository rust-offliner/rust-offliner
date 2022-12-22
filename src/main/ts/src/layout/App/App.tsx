import Map from '../../components/Map/Map'
import Footer from '../footer/Footer'
import Header from '../header/Header'
import styles from './App.module.scss'

export default function App() {
  return (
    <div className={styles.app}>
      <Header />
      <Map />
      <Footer />
    </div>
  )
}

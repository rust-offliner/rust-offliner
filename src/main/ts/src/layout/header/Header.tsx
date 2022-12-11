import Search from "../../components/header/search/Search"
import styles from "./header.module.scss"

export default function Header() {
  return (
    <header className={styles.header}>
      <picture>logo</picture>
      <Search />
      <div>login</div>
    </header>
  )
}

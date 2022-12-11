import ServerInfo from "../../components/main/serverInfo/ServerInfo"
import ServerMap from "../../components/main/serverMap/ServerMap"
import styles from "./main.module.scss"

export default function Main() {
  return (
    <main className={styles.main}>
      <aside>
        <ServerInfo></ServerInfo>
      </aside>
      <ServerMap></ServerMap>
    </main>
  )
}

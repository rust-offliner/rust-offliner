import Search from "../../components/header/Search";
import ServerInfo from "../../components/header/ServerInfo";

export default function Header() {
  return (
    <header>
      <ServerInfo />
      <Search />
    </header>
  );
}

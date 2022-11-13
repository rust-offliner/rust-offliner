export default function ServerInfo() {
  const serverName: string = "Lorem ipsum dolor sit amet.",
    onlinePlayers: number = 200,
    maxOnlinePlayers: number = 250;

    //todo get data from api
  return (
    <p>
      <span>{serverName}</span>
      <span>
        {onlinePlayers}/{maxOnlinePlayers}
      </span>
    </p>
  );
}

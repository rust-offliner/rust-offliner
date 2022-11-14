export default function ServerInfo() {
  const serverName = 'Lorem ipsum dolor sit amet.',
    onlinePlayers = 200,
    maxOnlinePlayers = 250

  // todo get data from api
  return (
    <p>
      <span>{serverName}</span>{' '}
      <span>
        {onlinePlayers} / {maxOnlinePlayers}
      </span>
    </p>
  )
}

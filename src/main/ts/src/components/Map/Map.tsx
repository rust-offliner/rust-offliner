import { CRS, LatLngBoundsExpression } from 'leaflet'
import { useState } from 'react'
import { MapContainer, ImageOverlay, LayersControl } from 'react-leaflet'
import CustomZoomControl from './Custom/ZoomControl/CustomZoomControl'
import styles from './Map.module.scss'

export default function Map() {
  const [mapURL, setMapURL] = useState(
    'https://files.rustmaps.com/img/231/2ad3818c-093c-46ec-bbc2-e52741116e99/FullMap.png',
  )
  const [gridURL, setGridURL] = useState(
    'https://files.rustmaps.com/grids/4500.png',
  )
  const [bounds, setBounds] = useState<LatLngBoundsExpression>([
    [0, 0],
    [1440, 1440],
  ])

  return (
    <MapContainer
      crs={CRS.Simple}
      center={[720, 720]}
      className={styles.a}
      zoom={0}
      minZoom={-0.5}
      maxZoom={2}
      scrollWheelZoom={true}
      zoomControl={false}
      attributionControl={false}
      doubleClickZoom={false}
    >
      <ImageOverlay url={mapURL} bounds={bounds} zIndex={-1} />
      <LayersControl>
        <LayersControl.Overlay checked name='Grid'>
          <ImageOverlay url={gridURL} bounds={bounds} zIndex={1}></ImageOverlay>
        </LayersControl.Overlay>
      </LayersControl>
      <CustomZoomControl bounds={bounds} />
    </MapContainer>
  )
}

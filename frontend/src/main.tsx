import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import './index.css'
import { createBrowserRouter, RouterProvider } from 'react-router'
import LoginPage from './pages/LoginPage.tsx'

const router = createBrowserRouter([
  {
    path:"/",
    children:[
      {
        index:true,
        element:<LoginPage/>
      }
    ]
  }
])

createRoot(document.getElementById('root')!).render(
  <StrictMode>
    <RouterProvider router={router}></RouterProvider>
  </StrictMode>,
)

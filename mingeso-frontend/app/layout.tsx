import type { Metadata } from 'next'
import './globals.css'
import { Inter as FontSans } from "next/font/google"
import { cn } from "@/lib/utils"
import { Navbar } from '@/components/navbar'

export const fontSans = FontSans({
  subsets: ["latin"],
  variable: "--font-sans",
})

export const metadata: Metadata = {
  title: 'Inscripción Asignaturas USACH',
  description: 'Plataforma para la inscripción de asignaturas USACH (Proyecto MINGESO)',
}

export default function RootLayout({
  children,
}: {
  children: React.ReactNode
}) {
  return (
    <html lang="es">
      <body
        className={cn(
          "min-h-screen bg-background font-sans antialiased",
          fontSans.variable
        )}
      >      
        <div className='w-full flex justify-center'>
          <div className='flex flex-col items-center w-[1400px]'>
            <Navbar />
            <div className='w-full flex flex-col justify-start'>
              {children}              
            </div>
          </div>
        </div>
      </body>
    </html>
  )
}

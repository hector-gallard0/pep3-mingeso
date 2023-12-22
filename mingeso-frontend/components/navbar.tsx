"use client";

import Image from "next/image";
import Link from "next/link"
import { usePathname } from "next/navigation"

const links = [
  {
    href: "/",
    label: "Inicio",
  },
  {
    href: "/horarios/ingresar",
    label: "Ingresar horarios",
  },
  {
    href: "/asignaturas/inscribir",
    label: "Inscribir asignaturas",
  }
]

export function Navbar() {
  const pathname = usePathname();

  return(
    <nav className="px-6 pb-1 flex justify-between items-center w-full border-b-[1px] border-amber-400 mb-6">      
        <div>
          <Image src="/images/Usach SV1.png" width={ 200 } height={ 50 } alt="Logo" />
        </div>
        <div>
          <ul className="flex gap-3">
            {links.map(( link, index ) => 
              <li key={ index }>
                <Link href={ link.href }>
                  <span className={`px-3 py-2 rounded-md ${pathname === link.href && "bg-teal-100 text-teal-600 font-medium"}`}>{ link.label }</span>
                </Link>     
              </li>
            )}
          </ul>
        </div>
    </nav>
  )
}
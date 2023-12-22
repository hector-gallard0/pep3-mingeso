import IngresarHorarioForm from "@/components/ingresar-horario-form";

import { Button } from "@/components/ui/button"
import {
  Card,
  CardContent,
  CardHeader,
  CardTitle,
} from "@/components/ui/card"

export default async function IngresarHorariosPage(){
  const response = await fetch('http://localhost:8081/api/carreras');
  const carreras = await response.json();
  console.log(carreras);

  return(
    <div>      
      <Card>
        <CardHeader>
          <CardTitle>Ingresar horarios</CardTitle>
          {/* <CardDescription>Selecciona la carrera, el plan de estudios y por último el horario que deseas ingresar.</CardDescription> */}
        </CardHeader>
        <CardContent>              
          <IngresarHorarioForm carreras={carreras} />            
        </CardContent>                
      </Card>      
    </div>
  )
}
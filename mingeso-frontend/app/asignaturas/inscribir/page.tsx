import { SeleccionarEstudianteForm } from "@/components/seleccionar-estudiante-form";
import {
  Card,
  CardContent,
  CardHeader,
  CardTitle,
} from "@/components/ui/card"
 
export default async function InscribirAsignaturasPage(){
  const response = await fetch('http://localhost:8081/api/carreras');
  const carreras = await response.json();
  console.log(carreras);

  return(
    <div className="mb-3">      
      <SeleccionarEstudianteForm />
    </div>
  )
}
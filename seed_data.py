#!/usr/bin/env python3
import json
import urllib.request
import urllib.error
import sys
from datetime import datetime

# =========================================================================
# CONFIGURACIÓN DEL SCRIPT
# =========================================================================
# URL de la API de producción de Alex Macias
DEFAULT_BASE_URL = "http://macias-admisiones.uaeftt-ute.site/api/"

# Credenciales de Administrador para obtener el Token JWT
ADMIN_USER = "admin"
ADMIN_PASS = "admin"

# =========================================================================
# LOGICA DE RED AUXILIAR
# =========================================================================
def make_request(url, method="GET", data=None, token=None):
    headers = {
        "Content-Type": "application/json",
        "Accept": "application/json"
    }
    if token:
        headers["Authorization"] = f"Bearer {token}"
        
    req_data = None
    if data:
        req_data = json.dumps(data).encode("utf-8")
        
    req = urllib.request.Request(url, data=req_data, headers=headers, method=method)
    
    try:
        with urllib.request.urlopen(req) as response:
            res_data = response.read().decode("utf-8")
            if res_data:
                return json.loads(res_data)
            return {}
    except urllib.error.HTTPError as e:
        try:
            err_content = e.read().decode("utf-8")
        except Exception:
            err_content = "No se pudo leer el cuerpo de error"
        print(f"[ERROR] Error HTTP {e.code} en {method} {url}")
        print(f"        Detalle del Servidor: {err_content}")
        raise e
    except Exception as e:
        print(f"[ERROR] Error de conexion: {str(e)}")
        raise e

# =========================================================================
# EXECUCIÓN DEL SEMBRADO DE REGISTROS DE ADMISIÓN
# =========================================================================
def main():
    base_url = DEFAULT_BASE_URL
    if len(sys.argv) > 1:
        base_url = sys.argv[1]
        if not base_url.endswith("/"):
            base_url += "/"
            
    print("=" * 60)
    print("INICIANDO SEMBRADO DE REGISTROS DE ADMISION EN EL SISTEMA")
    print(f"Apuntando a API: {base_url}")
    print("=" * 60)
    
    # 1. Obtención del token JWT de Administrador
    print("\n1. Autenticando Administrador...")
    login_url = f"{base_url}auth/login/"
    try:
        auth_res = make_request(login_url, "POST", {"username": ADMIN_USER, "password": ADMIN_PASS})
        token = auth_res.get("access")
        print("   [OK] Autenticacion Exitosa. Token JWT obtenido.")
    except Exception:
        print("   [ERROR] Error critico al iniciar sesion. Verifica las credenciales de Admin en la API.")
        return

    # 2. Creación de Usuarios (Docentes y Nuevos Estudiantes para Admisión)
    print("\n2. Registrando usuarios en el sistema...")
    register_url = f"{base_url}auth/register/"
    
    # Registraremos más estudiantes con nombres realistas ecuatorianos
    users_to_create = [
        # Docentes
        {"username": "carlos_docente", "email": "carlos.lara@admisiones.edu.ec", "password": "User1234!", "first_name": "Carlos", "last_name": "Lara"},
        {"username": "maria_docente", "email": "maria.gomez@admisiones.edu.ec", "password": "User1234!", "first_name": "Maria", "last_name": "Gomez"},
        {"username": "fernando_docente", "email": "fernando.vaca@admisiones.edu.ec", "password": "User1234!", "first_name": "Fernando", "last_name": "Vaca"},
        
        # Estudiantes (Proceso de Admisión)
        {"username": "alex_estudiante", "email": "alex.macias@estudiantes.edu.ec", "password": "User1234!", "first_name": "Alex", "last_name": "Macias"},
        {"username": "danny_estudiante", "email": "danny.perez@estudiantes.edu.ec", "password": "User1234!", "first_name": "Danny", "last_name": "Perez"},
        {"username": "jose_estudiante", "email": "jose.delgado@estudiantes.edu.ec", "password": "User1234!", "first_name": "Jose", "last_name": "Delgado"},
        {"username": "ana_estudiante", "email": "ana.mendoza@estudiantes.edu.ec", "password": "User1234!", "first_name": "Ana", "last_name": "Mendoza"},
        {"username": "luis_estudiante", "email": "luis.ortiz@estudiantes.edu.ec", "password": "User1234!", "first_name": "Luis", "last_name": "Ortiz"},
        {"username": "gabriela_estudiante", "email": "gaby.solis@estudiantes.edu.ec", "password": "User1234!", "first_name": "Gabriela", "last_name": "Solis"}
    ]
    
    user_ids = {}
    for user_data in users_to_create:
        try:
            print(f"   Creando usuario '{user_data['username']}'...")
            res = make_request(register_url, "POST", user_data)
            user_ids[user_data['username']] = res.get("id")
            print(f"   [OK] Creado con ID: {res.get('id')}")
        except urllib.error.HTTPError as e:
            if e.code == 400:
                print(f"   [WARNING] El usuario '{user_data['username']}' ya existe. Saltando creacion...")
            else:
                print("   [ERROR] Error inesperado al registrar usuario.")
        except Exception:
            print("   [ERROR] Error inesperado al registrar usuario.")

    # 3. Creación de Facultades
    print("\n3. Creando Facultades...")
    facultad_url = f"{base_url}facultades/"
    facultades_to_create = [
        {"nombre": "Facultad de Ingenieria y Tecnologias", "codigo": "FIT", "descripcion": "Ingenierias y Ciencias de la Computacion", "activo": True},
        {"nombre": "Facultad de Ciencias de la Salud", "codigo": "FCS", "descripcion": "Medicina, Enfermeria y Nutricion", "activo": True},
        {"nombre": "Facultad de Ciencias Administrativas", "codigo": "FCA", "descripcion": "Negocios, Economia y Finanzas", "activo": True}
    ]
    
    facultad_ids = []
    for fac in facultades_to_create:
        try:
            res = make_request(facultad_url, "POST", fac, token)
            facultad_ids.append(res.get("id"))
            print(f"   [OK] Facultad '{fac['nombre']}' creada con ID: {res.get('id')}")
        except Exception as e:
            print(f"   [ERROR] Al crear facultad '{fac['nombre']}': {str(e)}")

    if not facultad_ids:
        print("[ERROR] No se pudieron crear facultades. Abortando sembrado...")
        return

    # 4. Creación de Carreras
    print("\n4. Creando Carreras...")
    carrera_url = f"{base_url}carreras/"
    carreras_to_create = [
        {"facultad": facultad_ids[0], "nombre": "Ingenieria de Software", "codigo": "ISW", "duracion_semestres": 9, "activo": True},
        {"facultad": facultad_ids[0], "nombre": "Ciberseguridad", "codigo": "CIB", "duracion_semestres": 8, "activo": True},
        {"facultad": facultad_ids[1] if len(facultad_ids) > 1 else facultad_ids[0], "nombre": "Enfermeria", "codigo": "ENF", "duracion_semestres": 8, "activo": True},
        {"facultad": facultad_ids[2] if len(facultad_ids) > 2 else facultad_ids[0], "nombre": "Administracion de Empresas", "codigo": "ADM", "duracion_semestres": 8, "activo": True}
    ]
    
    carrera_ids = []
    for car in carreras_to_create:
        try:
            res = make_request(carrera_url, "POST", car, token)
            carrera_ids.append(res.get("id"))
            print(f"   [OK] Carrera '{car['nombre']}' creada con ID: {res.get('id')}")
        except Exception as e:
            print(f"   [ERROR] Al crear carrera '{car['nombre']}': {str(e)}")

    if not carrera_ids:
        print("[ERROR] No se pudieron crear carreras. Abortando...")
        return

    # 5. Creación de Docentes
    print("\n5. Creando Docentes...")
    docente_url = f"{base_url}docentes/"
    
    docentes_to_create = []
    if "carlos_docente" in user_ids:
        docentes_to_create.append({"user": user_ids["carlos_docente"], "cedula": "1712345678", "telefono": "0998765432", "especialidad": "Desarrollo Movil y Web", "activo": True})
    if "maria_docente" in user_ids:
        docentes_to_create.append({"user": user_ids["maria_docente"], "cedula": "1723456789", "telefono": "0997654321", "especialidad": "Arquitectura de Software", "activo": True})
    if "fernando_docente" in user_ids:
        docentes_to_create.append({"user": user_ids["fernando_docente"], "cedula": "1734567890", "telefono": "0996543210", "especialidad": "Administracion y Negocios", "activo": True})
        
    if not docentes_to_create:
        print("   [WARNING] Los IDs de usuario docente no se recuperaron. Se usan fallbacks...")
        docentes_to_create = [
            {"user": 2, "cedula": "1712345678", "telefono": "0998765432", "especialidad": "Desarrollo Movil y Web", "activo": True},
            {"user": 3, "cedula": "1723456789", "telefono": "0997654321", "especialidad": "Arquitectura de Software", "activo": True}
        ]

    docente_ids = []
    for doc in docentes_to_create:
        try:
            res = make_request(docente_url, "POST", doc, token)
            docente_ids.append(res.get("id"))
            print(f"   [OK] Docente creado con ID: {res.get('id')} (Cedula: {doc['cedula']})")
        except Exception as e:
            print(f"   [ERROR] Al crear docente con user {doc['user']}: {str(e)}")

    if not docente_ids:
        docente_ids = [1]
        print("   [WARNING] Usando Docente ID 1 como fallback.")

    # 6. Creación de Estudiantes (Nuevos admitidos y en proceso)
    print("\n6. Creando Estudiantes en Admision...")
    estudiante_url = f"{base_url}estudiantes/"
    
    estudiantes_to_create = []
    # Registraremos a los estudiantes vinculando sus carreras
    if "alex_estudiante" in user_ids:
        estudiantes_to_create.append({"user": user_ids["alex_estudiante"], "carrera": carrera_ids[0], "cedula": "1711122233", "telefono": "0991112223", "semestre_actual": 1, "activo": True})
    if "danny_estudiante" in user_ids:
        estudiantes_to_create.append({"user": user_ids["danny_estudiante"], "carrera": carrera_ids[0], "cedula": "1722233344", "telefono": "0992223334", "semestre_actual": 1, "activo": True})
    if "jose_estudiante" in user_ids:
        estudiantes_to_create.append({"user": user_ids["jose_estudiante"], "carrera": carrera_ids[1] if len(carrera_ids) > 1 else carrera_ids[0], "cedula": "1733344455", "telefono": "0993334445", "semestre_actual": 2, "activo": True})
    if "ana_estudiante" in user_ids:
        estudiantes_to_create.append({"user": user_ids["ana_estudiante"], "carrera": carrera_ids[2] if len(carrera_ids) > 2 else carrera_ids[0], "cedula": "1744455566", "telefono": "0994445556", "semestre_actual": 1, "activo": True})
    if "luis_estudiante" in user_ids:
        estudiantes_to_create.append({"user": user_ids["luis_estudiante"], "carrera": carrera_ids[3] if len(carrera_ids) > 3 else carrera_ids[0], "cedula": "1755566677", "telefono": "0995556667", "semestre_actual": 1, "activo": True})
    if "gabriela_estudiante" in user_ids:
        estudiantes_to_create.append({"user": user_ids["gabriela_estudiante"], "carrera": carrera_ids[0], "cedula": "1766677788", "telefono": "0996667778", "semestre_actual": 1, "activo": True})

    if not estudiantes_to_create:
        # Fallbacks
        estudiantes_to_create = [
            {"user": 4, "carrera": carrera_ids[0], "cedula": "1798765432", "telefono": "0981234567", "semestre_actual": 1, "activo": True},
            {"user": 5, "carrera": carrera_ids[0], "cedula": "1787654321", "telefono": "0987654321", "semestre_actual": 1, "activo": True}
        ]

    estudiante_ids = []
    for est in estudiantes_to_create:
        try:
            res = make_request(estudiante_url, "POST", est, token)
            estudiante_ids.append(res.get("id"))
            print(f"   [OK] Estudiante '{est['cedula']}' creado con ID: {res.get('id')}")
        except Exception as e:
            print(f"   [ERROR] Al crear estudiante: {str(e)}")

    if not estudiante_ids:
        estudiante_ids = [1]
        print("   [WARNING] Usando Estudiante ID 1 como fallback.")

    # 7. Creación de Materias (Cursos de Admisión / Nivelación y Especialidad)
    print("\n7. Creando Materias de Nivelacion...")
    materia_url = f"{base_url}materias/"
    materias_to_create = [
        # Para Ingeniería de Software e Ingeniería en General
        {"carrera": carrera_ids[0], "docente": docente_ids[0], "nombre": "Curso de Nivelacion en Programacion", "codigo": "NIV-PROG", "creditos": 3, "semestre": 1, "activo": True},
        {"carrera": carrera_ids[0], "docente": docente_ids[1] if len(docente_ids) > 1 else docente_ids[0], "nombre": "Introduccion a la Ingenieria de Software", "codigo": "NIV-INTRO-ISW", "creditos": 4, "semestre": 1, "activo": True},
        {"carrera": carrera_ids[0], "docente": docente_ids[0], "nombre": "Matematicas Elementales para Admision", "codigo": "NIV-MAT-1", "creditos": 4, "semestre": 1, "activo": True},
        
        # Para Ciberseguridad
        {"carrera": carrera_ids[1] if len(carrera_ids) > 1 else carrera_ids[0], "docente": docente_ids[1] if len(docente_ids) > 1 else docente_ids[0], "nombre": "Fundamentos de Redes de Computadoras", "codigo": "NIV-RED-1", "creditos": 3, "semestre": 1, "activo": True},
        
        # Para Enfermería
        {"carrera": carrera_ids[2] if len(carrera_ids) > 2 else carrera_ids[0], "docente": docente_ids[1] if len(docente_ids) > 1 else docente_ids[0], "nombre": "Biologia Celular y Anatomia Basica", "codigo": "NIV-BIO-ANAT", "creditos": 5, "semestre": 1, "activo": True},
        
        # Para Administración de Empresas
        {"carrera": carrera_ids[3] if len(carrera_ids) > 3 else carrera_ids[0], "docente": docente_ids[2] if len(docente_ids) > 2 else docente_ids[0], "nombre": "Introduccion a la Administracion y Negocios", "codigo": "NIV-INTRO-ADM", "creditos": 4, "semestre": 1, "activo": True}
    ]
    
    materia_ids = []
    for mat in materias_to_create:
        try:
            res = make_request(materia_url, "POST", mat, token)
            materia_ids.append(res.get("id"))
            print(f"   [OK] Materia '{mat['nombre']}' creada con ID: {res.get('id')}")
        except Exception as e:
            print(f"   [ERROR] Al crear materia '{mat['nombre']}': {str(e)}")

    if not materia_ids:
        print("[ERROR] No se pudieron crear materias. Abortando...")
        return

    # 8. Creación de Matrículas (Simulando admisiones del periodo actual 2026-I)
    print("\n8. Creando Matriculas Academicas...")
    matricula_url = f"{base_url}matriculas/"
    fecha_hoy = datetime.now().strftime("%Y-%m-%d")
    
    # Matricular a los estudiantes en sus materias correspondientes
    matriculas_to_create = []
    
    # Alex Macias matriculado en Nivelación de Programación y Matemáticas
    if len(estudiante_ids) > 0 and len(materia_ids) > 2:
        matriculas_to_create.append({"estudiante": estudiante_ids[0], "materia": materia_ids[0], "periodo": "2026-I", "estado": "activa", "fecha_matricula": fecha_hoy})
        matriculas_to_create.append({"estudiante": estudiante_ids[0], "materia": materia_ids[2], "periodo": "2026-I", "estado": "activa", "fecha_matricula": fecha_hoy})
        
    # Danny Perez matriculado en Nivelación de Programación e Introducción
    if len(estudiante_ids) > 1 and len(materia_ids) > 1:
        matriculas_to_create.append({"estudiante": estudiante_ids[1], "materia": materia_ids[0], "periodo": "2026-I", "estado": "activa", "fecha_matricula": fecha_hoy})
        matriculas_to_create.append({"estudiante": estudiante_ids[1], "materia": materia_ids[1], "periodo": "2026-I", "estado": "activa", "fecha_matricula": fecha_hoy})
        
    # José Delgado en Ciberseguridad/Redes
    if len(estudiante_ids) > 2 and len(materia_ids) > 3:
        matriculas_to_create.append({"estudiante": estudiante_ids[2], "materia": materia_ids[3], "periodo": "2026-I", "estado": "activa", "fecha_matricula": fecha_hoy})
        
    # Ana Mendoza en Biología (Enfermería)
    if len(estudiante_ids) > 3 and len(materia_ids) > 4:
        matriculas_to_create.append({"estudiante": estudiante_ids[3], "materia": materia_ids[4], "periodo": "2026-I", "estado": "activa", "fecha_matricula": fecha_hoy})
        
    # Luis Ortiz en Administración
    if len(estudiante_ids) > 4 and len(materia_ids) > 5:
        matriculas_to_create.append({"estudiante": estudiante_ids[4], "materia": materia_ids[5], "periodo": "2026-I", "estado": "activa", "fecha_matricula": fecha_hoy})
        
    # Gabriela Solís en Nivelación de Programación
    if len(estudiante_ids) > 5:
        matriculas_to_create.append({"estudiante": estudiante_ids[5], "materia": materia_ids[0], "periodo": "2026-I", "estado": "activa", "fecha_matricula": fecha_hoy})

    if not matriculas_to_create:
        matriculas_to_create = [
            {"estudiante": estudiante_ids[0], "materia": materia_ids[0], "periodo": "2026-I", "estado": "activa", "fecha_matricula": fecha_hoy}
        ]

    matricula_ids = []
    for mat in matriculas_to_create:
        try:
            res = make_request(matricula_url, "POST", mat, token)
            matricula_ids.append(res.get("id"))
            print(f"   [OK] Matricula creada con ID: {res.get('id')} (Estudiante ID {mat['estudiante']} -> Materia ID {mat['materia']})")
        except Exception as e:
            print(f"   [ERROR] Al crear matricula: {str(e)}")

    # 9. Creación de Notas (Cargando notas de admisión)
    print("\n9. Creando Notas de Evaluacion de Admision...")
    nota_url = f"{base_url}notas/"
    
    # Definiremos notas variadas para simular alumnos aprobados y reprobados
    # Nota: Matricula 0 y 1 -> Alex Macias
    #       Matricula 2 y 3 -> Danny Perez
    #       Matricula 4 -> Jose Delgado
    #       Matricula 5 -> Ana Mendoza
    #       Matricula 6 -> Luis Ortiz
    #       Matricula 7 -> Gabriela Solis
    
    calificaciones = [
        {"parcial1": 9.0, "parcial2": 9.5, "examen_final": 9.0}, # Alex (Excellence)
        {"parcial1": 8.0, "parcial2": 8.5, "examen_final": 8.5}, # Alex (Aprobado)
        {"parcial1": 7.5, "parcial2": 7.0, "examen_final": 7.0}, # Danny (Aprobado)
        {"parcial1": 6.0, "parcial2": 5.5, "examen_final": 6.5}, # Danny (Reprobado / Bajo rendimiento)
        {"parcial1": 8.5, "parcial2": 8.0, "examen_final": 9.0}, # Jose (Aprobado)
        {"parcial1": 9.5, "parcial2": 9.0, "examen_final": 9.5}, # Ana (Excellence)
        {"parcial1": 7.0, "parcial2": 8.0, "examen_final": 7.5}, # Luis (Aprobado)
        {"parcial1": 5.0, "parcial2": 4.5, "examen_final": 4.0}  # Gabriela (Reprobado)
    ]
    
    for idx, mat_id in enumerate(matricula_ids):
        # Tomamos una calificación de la lista de forma segura
        calif = calificaciones[idx % len(calificaciones)]
        payload = {
            "matricula": mat_id,
            "parcial1": calif["parcial1"],
            "parcial2": calif["parcial2"],
            "examen_final": calif["examen_final"]
        }
        try:
            res = make_request(nota_url, "POST", payload, token)
            print(f"   [OK] Nota creada para Matricula {mat_id} -> Final: {res.get('nota_final')} (Aprobado: {res.get('aprobado')})")
        except Exception as e:
            print(f"   [ERROR] Al crear nota para matricula {mat_id}: {str(e)}")

    print("\n" + "=" * 60)
    print("PROCESO DE SEMBRADO DE ADMISIONES COMPLETADO CON EXITO!")
    print("=" * 60)

if __name__ == "__main__":
    main()

<?php
    $con = mysqli_connect("localhost","root","","qrmascota");

    $usuario =$_POST["usuario"];
    $contrasena =$_POST["contrasena"];

    $statement = mysqli_prepare($con, "SELECT * FROM user WHERE usuario=? AND contrasena = ?");
    mysqli_stmt_bind_param($statement, "ss", $usuario, $contrasena);
    mysqli_stmt_execute($statement);

    mysqli_stmt_store_result($statement);
    mysqli_stmt_bind_result($statement,$userid_persona, $nombre,$apellido, $email, $celular,$contrasena);

    $response = array ();
    $response["succes"]=false;
    while (mysqli_stmt_fetch($statement))
    {
        $response["succes"]=true;
        $response["nombre"]=$nombre;
        $response["apellido"]=$apellido;
        $response["email"]=$email;
        $response["celular"]=$celular;
        $response["contrasena"]=$contrasena;

    }
    echo json_encode($response);


?>
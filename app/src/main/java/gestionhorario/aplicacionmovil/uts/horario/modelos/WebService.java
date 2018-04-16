package gestionhorario.aplicacionmovil.uts.horario.modelos;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.List;

import gestionhorario.aplicacionmovil.uts.horario.DataInfo;
import gestionhorario.aplicacionmovil.uts.horario.objetos.Parametros;

/**
 * Created by Tecnologia-6 on 14/04/2018.
 */

public class WebService {

    public WebService() {
    }

    public static SoapObject getConexion(final String METHOD_NAME, final List<Parametros> parametros) {
        final SoapObject[] resultado = new SoapObject[1];
        new Thread(new Runnable() {
            @Override
            public void run() {
                String SOAP_ACTION = DataInfo.NAMESPACE + METHOD_NAME;

                try {
                    SoapObject request = new SoapObject(DataInfo.NAMESPACE, METHOD_NAME);
                    for (int i = 0; i < parametros.size(); i++) {
                        request.addProperty(parametros.get(i).getNombre(), parametros.get(i).getValor());
                    }
                    SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                    soapEnvelope.dotNet = false;
                    soapEnvelope.setOutputSoapObject(request);
                    HttpTransportSE transport = new HttpTransportSE(DataInfo.URL);
                    transport.call(SOAP_ACTION, soapEnvelope);
                    resultado[0] = (SoapObject) soapEnvelope.getResponse();

                } catch (Exception e) {
                    resultado[0] = null;
                }
            }
        }).start();
        return resultado[0];
    }
}

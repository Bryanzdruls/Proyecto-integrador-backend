package proyecto.integrador.app.services.user.xmlreport;

import org.w3c.dom.*;
import proyecto.integrador.app.entities.User;

import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.List;

public class XmlReportGenerator {

    public ByteArrayInputStream generateUserXmlReport(List<User> users) throws Exception {
        double totalReward = users.stream()
                .mapToDouble(user -> user.getRewardValue() != null ? user.getRewardValue() : 0)
                .sum();

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.newDocument();

        Element root = doc.createElement("UsersReport");
        doc.appendChild(root);

        Element totalElement = doc.createElement("TotalReward");
        totalElement.setTextContent(String.valueOf(totalReward));
        root.appendChild(totalElement);

        Element usersElement = doc.createElement("Users");
        root.appendChild(usersElement);

        for (User user : users) {
            Element userElement = doc.createElement("User");

            Element name = doc.createElement("Name");
            name.setTextContent(user.getName());

            Element email = doc.createElement("Email");
            email.setTextContent(user.getEmail());

            Element reward = doc.createElement("RewardValue");
            reward.setTextContent(String.valueOf(user.getRewardValue()));

            Element percentage = doc.createElement("Percentage");
            Double rewardNull = user.getRewardValue();
            double pct = (rewardNull != null && totalReward > 0) ? (rewardNull / totalReward) * 100 : 0;
            percentage.setTextContent(String.format("%.2f", pct) + "%");

            userElement.appendChild(name);
            userElement.appendChild(email);
            userElement.appendChild(reward);
            userElement.appendChild(percentage);

            usersElement.appendChild(userElement);
        }

        // Escribir a memoria
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer = tf.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        transformer.transform(new DOMSource(doc), new StreamResult(outputStream));

        return new ByteArrayInputStream(outputStream.toByteArray());
    }

}

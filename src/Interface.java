import java.io.IOException;
import java.util.*;
//import javax.servlet.http.HttpServlet;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.bson.Document;
import main.java.Main;
public class Interface extends HttpServlet {

    String query=null;

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        StringBuilder page = new StringBuilder();

        query = request.getParameter("Query");
//        page.append("<!DOCTYPE html>\n" +
//                        "<html>" +
//                        "<body>" +
//                query +
//                        "</body>" +
//                        "</html>");

        response.setContentType("text/html");
        //logic of getting result
        List<Document>Docs=new ArrayList<>();
        Main obj =new Main(query);
        Docs=obj.search();

        //receive it in list of docs
        addbeforresult(page);

        //build the main structure of the page

        for(Document doc :Docs)
        {
            addresult(doc,page);
        }
        addafterresult(page);
//test();
        response.getWriter().println(page);
    }

    void test(StringBuilder page)
    {
        page.append("<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "\n" +
                "<head>\n" +
                "  <meta charset=\"UTF-8\" />\n" +
                "  <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\" />\n" +
                "  <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\" />\n" +
                "  <title>Navigation Bar with Search Box</title>\n" +
                "  <link rel=\"stylesheet\" href=\"https://unicons.iconscout.com/release/v4.0.0/css/line.css\" />\n" +
                "  <link rel=\"stylesheet\" href=\"https://cdn.jsdelivr.net/npm/bootstrap@4.1.3/dist/css/bootstrap.min.css\" integrity=\"sha384-MCw98/SFnGE8fJT3GXwEOngsV7Zt27NXFoaoApmYm81iuXoPkFOJwJ8ERdknLPMO\" crossorigin=\"anonymous\">\n" +
                "  <script src=\"https://code.jquery.com/jquery-3.3.1.slim.min.js\" integrity=\"sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo\" crossorigin=\"anonymous\"></script>\n" +
                "<script src=\"https://cdn.jsdelivr.net/npm/popper.js@1.14.3/dist/umd/popper.min.js\" integrity=\"sha384-ZMP7rVo3mIykV+2+9J3UJ46jBk0WLaUAdn689aCwoqbBJiSnjAK/l8WvCWPIPm49\" crossorigin=\"anonymous\"></script>\n" +
                "<script src=\"https://cdn.jsdelivr.net/npm/bootstrap@4.1.3/dist/js/bootstrap.min.js\" integrity=\"sha384-ChfqqxuZUCnJSK3+MXmPNIyE6ZbWh2IMqE241rYiqJxyMiZ6OW/JmZQ5stwEULTy\" crossorigin=\"anonymous\"></script>\n" +
                "<style>\n" +
                "@import url(\"https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600&display=swap\");\n" +
                ".pagination{\n" +
                "  width: 340px;\n" +
                "  border-radius: 35px;\n" +
                "  box-shadow: 0 0 20px 5px rgb(3, 3, 43);\n" +
                "  display: flex;\n" +
                "  align-items: center;\n" +
                "  justify-content: center;\n" +
                "  background-color: var(--purple);\n" +
                "    position: absolute;\n" +
                "    bottom: initial;\n" +
                "    left: 38%;"+
                "}\n" +
                ".pagination a{\n" +
                "  padding: 0px 18px;\n" +
                "  font-weight: 500;\n" +
                "  text-decoration: none;\n" +
                "  font-size: 20px;\n" +
                "  color: #ae52c9;\n" +
                "  border-radius: 50%;\n" +
                "  transition: 0.5s ease-in-out;\n" +
                "}\n" +
                ".control{\n" +
                "  font-size: 20px;\n" +
                "  font-weight: bold;\n" +
                "  background-color: transparent;\n" +
                "  color: rgb(200, 200, 239);\n" +
                "  border: none;\n" +
                "  cursor: pointer;\n" +
                "  padding: 15px 15px;\n" +
                "  border-radius: 25px;\n" +
                "  transition: 0.5s ease-in-out ;\n" +
                "}\n" +
                "\n" +
                ".control:not(.disabled):hover,.pageNumbers a:hover{\n" +
                "  background-color:#08010a;\n" +
                "  color: white;\n" +
                "}\n" +
                "a.active{\n" +
                "  background-color: #b4a2b9;\n" +
                "  color: white;\n" +
                "}\n" +
                ".hidden{\n" +
                "  display: none;\n" +
                "}\n" +
                "li{\n" +
                "  list-style-type: none;\n" +
                "} \n" +
                "* {\n" +
                "  margin: 0;\n" +
                "  padding: 0;\n" +
                "  box-sizing: border-box;\n" +
                "  font-family: \"Poppins\", sans-serif;\n" +
                "}\n" +
                "body {\n" +
                "  background: #08010a;\n" +
                "  color: blanchedalmond;\n" +
                "}\n" +
                ".nav {\n" +
                "  position: fixed;\n" +
                "  top: 0;\n" +
                "  left: 0;\n" +
                "  width: 100%;\n" +
                "  height: 14%;\n" +
                "  padding: 15px 200px;\n" +
                "  background-color:#16161cd4 ;\n" +
                "  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);\n" +
                "}\n" +
                ".nav,\n" +
                ".nav .nav-links {\n" +
                "  display: flex;\n" +
                "  align-items:center;\n" +
                "}\n" +
                ".nav {\n" +
                "  justify-content:first baseline;\n" +
                "}\n" +
                ".nav-content{\n" +
                "  display: block;\n" +
                "  \n" +
                "}\n" +
                "a {\n" +
                "  color:#caa1d5;\n" +
                "  text-decoration: none;\n" +
                "}\n" +
                ".nav .logo {\n" +
                "  font-size: 30px;\n" +
                "  font-weight: 500;\n" +
                "  position: absolute;\n" +
                "  left:3%;\n" +
                "  top:30%;\n" +
                "  background: linear-gradient(to right,#0C134F,#1D267D,#5C469C,#654E92,#D4ADFC,#6C9BCF,#A5C0DD,#EBD8B2);\n" +
                "  -webkit-background-clip: text;\n" +
                "  -webkit-text-fill-color: transparent;\n" +
                "  font-weight:bolder;\n" +
                "}\n" +
                ".search{\n" +
                "  position: relative;\n" +
                "  width: 700px; \n" +
                "  top:30%;\n" +
                "  right:20%;\n" +
                "  height: 60px;\n" +
                "  background:#fff;\n" +
                "  border-radius: 60px;\n" +
                "  transition: 0.5s;\n" +
                "  box-shadow:0 0 0 5px #ACB1D6;\n" +
                "  overflow: hidden;\n" +
                "  }\n" +
                "  .search .input\n" +
                "{\n" +
                "position: relative;\n" +
                "width: 300px;\n" +
                "height: 60px;\n" +
                "left: -8%;\n" +
                "size: 20px;\n" +
                "display: flex;\n" +
                "justify-content: center;\n" +
                "align-items: center;\n" +
                "top: -13%;\n" +
                "}\n" +
                "\n" +
                ".nav .nav-links {\n" +
                "  column-gap: 20px;\n" +
                "  list-style: none;\n" +
                "}\n" +
                ".nav .nav-links a {\n" +
                "  transition: all 0.2s linear;\n" +
                "}\n" +
                ".nav.openSearch .nav-links a {\n" +
                "  opacity: 0;\n" +
                "  pointer-events: none;\n" +
                "}\n" +
                ".nav .search-icon {\n" +
                "  color: #fff;\n" +
                "  font-size: 20px;\n" +
                "  cursor: pointer;\n" +
                "}\n" +
                ".nav .search-box {\n" +
                "  position: absolute;\n" +
                "  /* right: 250px; */\n" +
                "  height: 45px;\n" +
                "  left:20%;\n" +
                "  max-width: 555px;\n" +
                "  width: 100%;\n" +
                "  /* pointer-events: none; */\n" +
                "  transition: all 0.2s linear;\n" +
                "}\n" +
                ".nav.openSearch .search-box {\n" +
                "  opacity: 1;\n" +
                "  pointer-events: auto;\n" +
                "  top: auto;\n" +
                "  /* left: 20%; */\n" +
                "}\n" +
                ".search-box .search-icon {\n" +
                "  position: absolute;\n" +
                "  left: 15px;\n" +
                "  top: 50%;\n" +
                "  left: 15px;\n" +
                "  color: #4a98f7;\n" +
                "  transform: translateY(-50%);\n" +
                "}\n" +
                ".nav .navOpenBtn,\n" +
                ".nav .navCloseBtn {\n" +
                "  display: none;\n" +
                "}\n" +
                "\n" +
                "/* responsive */\n" +
                "@media screen and (max-width: 1160px) {\n" +
                "  .nav {\n" +
                "    padding: 15px 100px;\n" +
                "  }\n" +
                "  .nav .search-box {\n" +
                "    right: 100px;\n" +
                "  }\n" +
                "}\n" +
                "@media screen and (max-width: 950px) {\n" +
                "  .nav {\n" +
                "    padding: 15px 7px;\n" +
                "  }\n" +
                "  .nav .search-box {\n" +
                "    right: 237px;\n" +
                "    max-width: 400px;\n" +
                "  }\n" +
                "}\n" +
                "@media screen and (max-width: 768px) {\n" +
                "  .nav .navOpenBtn,\n" +
                "  .nav .navCloseBtn {\n" +
                "    display:block;\n" +
                "  }\n" +
                "  .nav {\n" +
                "    padding: 15px 20px;\n" +
                "  }\n" +
                "  .nav  {\n" +
                "    position: fixed;\n" +
                "    top: 0;\n" +
                "    left: -100%;\n" +
                "    height: 100%;\n" +
                "    max-width: 280px;\n" +
                "    width: 100%;\n" +
                "    padding-top: 100px;\n" +
                "    row-gap: 30px;\n" +
                "    flex-direction: column;\n" +
                "    background-color: #11101d;\n" +
                "    box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);\n" +
                "    transition: all 0.4s ease;\n" +
                "    z-index: 100;\n" +
                "  }\n" +
                "  .nav.openNav .nav-links {\n" +
                "    left: 0;\n" +
                "  }\n" +
                "  .nav .navOpenBtn {\n" +
                "    color: #fff;\n" +
                "    font-size: 20px;\n" +
                "    cursor: pointer;\n" +
                "  }\n" +
                "  .nav .navCloseBtn {\n" +
                "    position: absolute;\n" +
                "    top: 20px;\n" +
                "    right: 20px;\n" +
                "    color: #fff;\n" +
                "    font-size: 20px;\n" +
                "    cursor: pointer;\n" +
                "  }\n" +
                "  .nav .search-box {\n" +
                "    top: calc(100% + 10px);\n" +
                "    max-width: calc(100% - 20px);\n" +
                "    right: 50%;\n" +
                "    transform: translateX(50%);\n" +
                "    box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);\n" +
                "    }\n" +
                "}\n" +
                "\n" +
                ".BoxofResult{\n" +
                "  display: inline-block;\n" +
                "  position: absolute;\n" +
                "  top: 92px;\n" +
                "  width: 100%;\n" +
                "  height: 130%; \n" +
                "  z-index: -1;\n" +
                "}\n" +
                "\n" +
                ".result{\n" +
                "  border: #6f42c1;\n" +
                "  border-style:ridge;\n" +
                "  border-radius: 1.25rem;" +
                " margin: 2%;\n" +
                " padding: 1%;\n"+
                "  \n" +
                "}\n" +
                "\n" +
                ".search .icon\n" +
                "{\n" +
                "position: absolute;\n" +
                "top: -3px;\n" +
                "    left: 13px;\n" +
                "    width: 30px;\n" +
                "height: 60px;\n" +
                "background: #ffffff;\n" +
                "border-radius: 60px;\n" +
                "display: flex;\n" +
                "justify-content: center;\n" +
                "align-items: center;\n" +
                "z-index: 1000;\n" +
                "cursor: pointer;\n" +
                "}\n" +
                ".search .icon::before\n" +
                "{\n" +
                "content: '';\n" +
                "position: absolute;\n" +
                "width: 15px;\n" +
                "height: 15px;\n" +
                "border: 3px solid #FFEAD2;\n" +
                "border-radius: 50px;\n" +
                "transform: translate(-4px,-4px);\n" +
                "}\n" +
                ".search .icon::after\n" +
                "{\n" +
                "content: '';\n" +
                "position: absolute;\n" +
                "width: 3px;\n" +
                "height: 12px;\n" +
                "background:#FFEAD2;\n" +
                "transform: translate(6px,6px) rotate(315deg);\n" +
                "\n" +
                "}\n" +
                "input {\n" +
                "  position: relative;\n" +
                "  top: -6px;\n" +
                "  left: 20%;\n" +
                "  width:fit-content;\n" +
                "  height: 100%;\n" +
                "  border: none;\n" +
                "  outline: none;\n" +
                "  font-size: 18px;\n" +
                "  margin:0px;\n" +
                "}\n" +
                "\n" +
                "</style>\n" +
                "  <meta charset=\"UTF-8\" />\n" +
                "  <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\" />\n" +
                "  <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\" />\n" +
                "  <link rel=\"stylesheet\" href=\"css/bootstrap.min.css\" />\n" +
                "  <link rel=\"stylesheet\" href=\"css/all.min.css\" />\n" +
                "  <link rel=\"stylesheet\" href=\"css/main.css\" />\n" +
                "  <link rel=\"manifest\" href=\"favicon/manifest.json\" />\n" +
                "\n" +
                "\n" +
                "  <link\n" +
                "    rel=\"apple-touch-icon\"\n" +
                "    sizes=\"57x57\"\n" +
                "    href=\"favicon/apple-icon-57x57.png\"\n" +
                "  />\n" +
                "  <link\n" +
                "    rel=\"apple-touch-icon\"\n" +
                "    sizes=\"60x60\"\n" +
                "    href=\"favicon/apple-icon-60x60.png\"\n" +
                "  />\n" +
                "  <link\n" +
                "    rel=\"apple-touch-icon\"\n" +
                "    sizes=\"72x72\"\n" +
                "    href=\"favicon/apple-icon-72x72.png\"\n" +
                "  />\n" +
                "  <link\n" +
                "    rel=\"apple-touch-icon\"\n" +
                "    sizes=\"76x76\"\n" +
                "    href=\"favicon/apple-icon-76x76.png\"\n" +
                "  />\n" +
                "  <link\n" +
                "    rel=\"apple-touch-icon\"\n" +
                "    sizes=\"114x114\"\n" +
                "    href=\"favicon/apple-icon-114x114.png\"\n" +
                "  />\n" +
                "  <link\n" +
                "    rel=\"apple-touch-icon\"\n" +
                "    sizes=\"120x120\"\n" +
                "    href=\"favicon/apple-icon-120x120.png\"\n" +
                "  />\n" +
                "  <link\n" +
                "    rel=\"apple-touch-icon\"\n" +
                "    sizes=\"144x144\"\n" +
                "    href=\"favicon/apple-icon-144x144.png\"\n" +
                "  />\n" +
                "  <link\n" +
                "    rel=\"apple-touch-icon\"\n" +
                "    sizes=\"152x152\"\n" +
                "    href=\"favicon/apple-icon-152x152.png\"\n" +
                "  />\n" +
                "  <link\n" +
                "    rel=\"apple-touch-icon\"\n" +
                "    sizes=\"180x180\"\n" +
                "    href=\"favicon/apple-icon-180x180.png\"\n" +
                "  />\n" +
                "  <link\n" +
                "    rel=\"icon\"\n" +
                "    type=\"image/png\"\n" +
                "    sizes=\"192x192\"\n" +
                "    href=\"favicon/android-icon-192x192.png\"\n" +
                "  />\n" +
                "  <link\n" +
                "    rel=\"icon\"\n" +
                "    type=\"image/png\"\n" +
                "    sizes=\"32x32\"\n" +
                "    href=\"favicon/favicon-32x32.png\"\n" +
                "  />\n" +
                "  <link\n" +
                "    rel=\"icon\"\n" +
                "    type=\"image/png\"\n" +
                "    sizes=\"96x96\"\n" +
                "    href=\"favicon/favicon-96x96.png\"\n" +
                "  />\n" +
                "  <link\n" +
                "    rel=\"icon\"\n" +
                "    type=\"image/png\"\n" +
                "    sizes=\"16x16\"\n" +
                "    href=\"favicon/favicon-16x16.png\"\n" +
                "  />\n" +
                "  <meta name=\"msapplication-TileColor\" content=\"#ffffff\" />\n" +
                "  <meta name=\"msapplication-TileImage\" content=\"ms-icon-144x144.png\" />\n" +
                "  <meta name=\"theme-color\" content=\"#ffffff\" />\n" +
                "  <style></style>\n" +
                "</head>\n" +
                "\n" +
                "<body>\n" +
                "  <nav class=\"nav\">\n" +
                "       <a href=\"http://localhost:8080/SearchEngine.html\" class=\"logo \">GOWELL</a>\n" +
                "\n" +
                "    <div class=\"search-box search\">\n" +
                "          <div class=\"icon\"></div>\n" +
                "          <form  method=\"get\" id=\"search-form\">\n" +
                "              <div class=\"input\">\n" +
                "                  <input type=\"search\" placeholder=\"Search\" id=\"letssearch\" name =\"Query\">\n" +
                "              </div>\n" +
                "          </form>\n" +
                "          <span class=\"clear\" onclick=\"document.getElementById('letssearch').value = '' ,\n" +
                "       document.getElementById('sentences-list').innerHTML=''\"></span>\n" +
                "      <button type=\"button\" id=\"voice-input-btn\"><i class=\"fas fa-microphone\"></i></button>\n" +
                "\n" +
                "  </div>\n" +
                "</div>\n" +
                "  </nav>\n" +
                "\n" +
                "  <div class=\"BoxofResult\">\n" +
                "\n" +
                "    <div class=\"container\">\n" +
                "      <div class=\"wrapper\">\n" +
                "          <div class=\"content\">\n" +
                "              <ul id=\"paginationList\">\n" +
                "                  <li>\n" +
                "                    <div class=\"search-results container mt-5 \">\n" +
                "                      <div class=\"  mb-3 \">\n" +
                "                        <div class=\"card-body result\">\n" +
                "                          <a class=\"card-title page-title\" href=\"#\">Title of the page 0</a>\n" +
                "                          <br>\n" +
                "                          <a href=\"#\" class=\"card-link link\">Card link</a>\n" +
                "                          <br>\n" +
                "                          <p class=\"card-text\">part of the page with have the <b>word</b></p>\n" +
                "                        </div>\n" +
                "                      </div>\n" +
                "                    </div>\n" +
                "                  </li>\n" +
                "\n" +
                "                  <li>\n" +
                "                    <div class=\"search-results container mt-5 \">\n" +
                "                      <div class=\"  mb-3 \">\n" +
                "                        <div class=\"card-body result\">\n" +
                "                          <a class=\"card-title page-title\" href=\"#\">Title of the page 0</a>\n" +
                "                          <br>\n" +
                "                          <a href=\"#\" class=\"card-link link\">Card link</a>\n" +
                "                          <br>\n" +
                "                          <p class=\"card-text\">part of the page with have the <b>word</b></p>\n" +
                "                        </div>\n" +
                "                      </div>\n" +
                "                    </div>\n" +
                "                  </li>\n" +
                "                  \n" +
                "                  <li>\n" +
                "                    <div class=\"search-results container mt-5 \">\n" +
                "                      <div class=\"  mb-3 \">\n" +
                "                        <div class=\"card-body result\">\n" +
                "                          <a class=\"card-title page-title\" href=\"#\">Title of the page 0</a>\n" +
                "                          <br>\n" +
                "                          <a href=\"#\" class=\"card-link link\">Card link</a>\n" +
                "                          <br>\n" +
                "                          <p class=\"card-text\">part of the page with have the <b>word</b></p>\n" +
                "                        </div>\n" +
                "                      </div>\n" +
                "                    </div>\n" +
                "                  </li>\n" +
                "                  <li>\n" +
                "                    <div class=\"search-results container mt-5 \">\n" +
                "                      <div class=\"  mb-3 \">\n" +
                "                        <div class=\"card-body result\">\n" +
                "                          <a class=\"card-title page-title\" href=\"#\">Title of the page 0</a>\n" +
                "                          <br>\n" +
                "                          <a href=\"#\" class=\"card-link link\">Card link</a>\n" +
                "                          <br>\n" +
                "                          <p class=\"card-text\">part of the page with have the <b>word</b></p>\n" +
                "                        </div>\n" +
                "                      </div>\n" +
                "                    </div>\n" +
                "                  </li>\n" +
                "                  <li>\n" +
                "                    <div class=\"search-results container mt-5 \">\n" +
                "                      <div class=\"  mb-3 \">\n" +
                "                        <div class=\"card-body result\">\n" +
                "                          <a class=\"card-title page-title\" href=\"#\">Title of the page 0</a>\n" +
                "                          <br>\n" +
                "                          <a href=\"#\" class=\"card-link link\">Card link</a>\n" +
                "                          <br>\n" +
                "                          <p class=\"card-text\">part of the page with have the <b>word</b></p>\n" +
                "                        </div>\n" +
                "                      </div>\n" +
                "                    </div>\n" +
                "                  </li>\n" +
                "                  <li>\n" +
                "                    <div class=\"search-results container mt-5 \">\n" +
                "                      <div class=\"  mb-3 \">\n" +
                "                        <div class=\"card-body result\">\n" +
                "                          <a class=\"card-title page-title\" href=\"#\">Title of the page 0</a>\n" +
                "                          <br>\n" +
                "                          <a href=\"#\" class=\"card-link link\">Card link</a>\n" +
                "                          <br>\n" +
                "                          <p class=\"card-text\">part of the page with have the <b>word</b></p>\n" +
                "                        </div>\n" +
                "                      </div>\n" +
                "                    </div>\n" +
                "                  </li>\n" +
                "                  <li>\n" +
                "                    <div class=\"search-results container mt-5 \">\n" +
                "                      <div class=\"  mb-3 \">\n" +
                "                        <div class=\"card-body result\">\n" +
                "                          <a class=\"card-title page-title\" href=\"#\">Title of the page 0</a>\n" +
                "                          <br>\n" +
                "                          <a href=\"#\" class=\"card-link link\">Card link</a>\n" +
                "                          <br>\n" +
                "                          <p class=\"card-text\">part of the page with have the <b>word</b></p>\n" +
                "                        </div>\n" +
                "                      </div>\n" +
                "                    </div>\n" +
                "                  </li>\n" +
                "                  <li>\n" +
                "                    <div class=\"search-results container mt-5 \">\n" +
                "                      <div class=\"  mb-3 \">\n" +
                "                        <div class=\"card-body result\">\n" +
                "                          <a class=\"card-title page-title\" href=\"#\">Title of the page 0</a>\n" +
                "                          <br>\n" +
                "                          <a href=\"#\" class=\"card-link link\">Card link</a>\n" +
                "                          <br>\n" +
                "                          <p class=\"card-text\">part of the page with have the <b>word</b></p>\n" +
                "                        </div>\n" +
                "                      </div>\n" +
                "                    </div>\n" +
                "                  </li>\n" +
                "                  <li>\n" +
                "                    <div class=\"search-results container mt-5 \">\n" +
                "                      <div class=\"  mb-3 \">\n" +
                "                        <div class=\"card-body result\">\n" +
                "                          <a class=\"card-title page-title\" href=\"#\">Title of the page 0</a>\n" +
                "                          <br>\n" +
                "                          <a href=\"#\" class=\"card-link link\">Card link</a>\n" +
                "                          <br>\n" +
                "                          <p class=\"card-text\">part of the page with have the <b>word</b></p>\n" +
                "                        </div>\n" +
                "                      </div>\n" +
                "                    </div>\n" +
                "                  </li>\n" +
                "                  <li>\n" +
                "                    <div class=\"search-results container mt-5 \">\n" +
                "                      <div class=\"  mb-3 \">\n" +
                "                        <div class=\"card-body result\">\n" +
                "                          <a class=\"card-title page-title\" href=\"#\">Title of the page 0</a>\n" +
                "                          <br>\n" +
                "                          <a href=\"#\" class=\"card-link link\">Card link</a>\n" +
                "                          <br>\n" +
                "                          <p class=\"card-text\">part of the page with have the <b>word</b></p>\n" +
                "                        </div>\n" +
                "                      </div>\n" +
                "                    </div>\n" +
                "                  </li>\n" +
                "                      </ul>\n" +
                "          </div>\n" +
                "      </div>\n" +
                "\n" +
                "\n" +
                "      <div class=\"pagination-container d-flex justify-content-center align-bottom\"  >\n" +
                "          <div class=\"pagination \" >\n" +
                "              <button class=\"control\" id=\"prev\" title=\"Previous page\">\n" +
                "                 <<<  Prev\n" +
                "              </button>\n" +
                "              <div class=\"pageNumbers\">\n" +
                "              </div>\n" +
                "              <button class=\"control\" id=\"next\" title=\"Next page\">\n" +
                "                  Next >>>\n" +
                "              </button>\n" +
                "          </div>\n" +
                "      </div>\n" +
                "  </div>\n" +
                "  <script>\n" +
                "    const pageNumbers = document.querySelector(\".pageNumbers\");\n" +
                "const paginationList = document.getElementById(\"paginationList\");\n" +
                "const listItems = paginationList.querySelectorAll(\"li\");\n" +
                "const prevButton = document.getElementById(\"prev\");\n" +
                "const nextButton = document.getElementById(\"next\");\n" +
                "\n" +
                "const contentLimit = 5;\n" +
                "const pageCount = Math.ceil(listItems.length / contentLimit);\n" +
                "let currentPage = 1;\n" +
                "\n" +
                "const displayPageNumbers = (index) =>{\n" +
                "    const pageNumber = document.createElement(\"a\");\n" +
                "    pageNumber.innerText = index;\n" +
                "    pageNumber.setAttribute('href', \"#\");\n" +
                "    pageNumber.setAttribute(\"index\", index);\n" +
                "    pageNumbers.appendChild(pageNumber);\n" +
                "};\n" +
                "\n" +
                "const getPageNumbers = ()=>{\n" +
                "    for(let i=1; i <= pageCount; i++){\n" +
                "        displayPageNumbers(i);\n" +
                "    };\n" +
                "};\n" +
                "\n" +
                "const disableButton = (button) =>{\n" +
                "    button.classList.add(\"disabled\");\n" +
                "    button.setAttribute(\"disabled\", true);\n" +
                "};\n" +
                "\n" +
                "const enableButton = (button) =>{\n" +
                "    button.classList.remove(\"disabled\");\n" +
                "    button.removeAttribute(\"disabled\");\n" +
                "};\n" +
                "\n" +
                "const controlButtonsStatus = () =>{\n" +
                "    if(currentPage == 1){\n" +
                "        disableButton(prevButton);\n" +
                "    }\n" +
                "    else{\n" +
                "        enableButton(prevButton);\n" +
                "    }\n" +
                "    if(pageCount == currentPage){\n" +
                "        disableButton(nextButton);\n" +
                "    }\n" +
                "    else{\n" +
                "        enableButton(nextButton);\n" +
                "    }\n" +
                "};\n" +
                "\n" +
                "const handleActivePageNumber = () =>{\n" +
                "    document.querySelectorAll('a').forEach((button) =>{\n" +
                "        button.classList.remove(\"active\");\n" +
                "        const pageIndex = Number(button.getAttribute(\"index\"));\n" +
                "        if(pageIndex == currentPage){\n" +
                "            button.classList.add('active');\n" +
                "        }\n" +
                "    });\n" +
                "};\n" +
                "\n" +
                "const setCurrentPage = (pageNum) =>{\n" +
                "    currentPage = pageNum;\n" +
                "\n" +
                "    handleActivePageNumber();\n" +
                "    controlButtonsStatus();\n" +
                "\n" +
                "    const prevRange = (pageNum -1) * contentLimit;\n" +
                "    const currRange = pageNum * contentLimit;\n" +
                "\n" +
                "    listItems.forEach((item, index) =>{\n" +
                "        item.classList.add('hidden');\n" +
                "        if(index >= prevRange && index < currRange){\n" +
                "            item.classList.remove('hidden');\n" +
                "        }\n" +
                "    });\n" +
                "};\n" +
                "\n" +
                "window.addEventListener('load', ()=>{\n" +
                "    getPageNumbers();\n" +
                "    setCurrentPage(1);\n" +
                "\n" +
                "    prevButton.addEventListener('click', ()=>{\n" +
                "        setCurrentPage(currentPage - 1);\n" +
                "    });\n" +
                "\n" +
                "    nextButton.addEventListener(\"click\", ()=>{\n" +
                "        setCurrentPage(currentPage + 1);\n" +
                "    });\n" +
                "\n" +
                "    document.querySelectorAll('a').forEach((button) =>{\n" +
                "        const pageIndex = Number(button.getAttribute('index'));\n" +
                "\n" +
                "        if(pageIndex){\n" +
                "            button.addEventListener('click', ()=>{\n" +
                "                setCurrentPage(pageIndex);\n" +
                "            });\n" +
                "        };\n" +
                "    });\n" +
                "});\n" +
                "\n" +
                "  </script>\n" +
                "    \n" +
                "  </div>\n" +
                "\n" +
                "</body>\n" +
                "</html>");
    }
    void addresult(Document Doc,StringBuilder page) {

        boldQueryWords(Doc);

        page.append(
                "    <li>\n" +
                        "                    <div class=\"search-results container mt-5 \">\n" +
                        "                      <div class=\"  mb-3 \">\n" +
                        "                        <div class=\"card-body result\">\n" +
                        "                          <a class=\"card-title page-title\" href=");
        page.append(Doc.getString("URL"));
        page.append(">" );
        page.append(Doc.getString("title"));

        page.append("</a>\n" +
                "                          <br>\n" +
                "                          <a class=\"card-link link\" href=" );
        page.append(Doc.getString("URL"));
        page.append(">" );
        page.append(Doc.getString("URL"));
        page.append("</a>\n" +
                "                          <br>\n" +
                "                          <p class=\"card-text\">");

        page.append(Doc.getString("paragraph"));
        page.append("</p>\n" +
                "                        </div>\n" +
                "                      </div>\n" +
                "                    </div>\n" +
                "                  </li>\n"
        );


    }
    private void addCss(StringBuilder page)
    {
        page.append("<style>/* Google Fonts - Poppins */\n" +
                "@import url(\"https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600&display=swap\");\n" +
                "\n" +
                "\n" +
                ".pagination{\n" +
                "  width: 340px;\n" +
                "  border-radius: 35px;\n" +
                "  box-shadow: 0 0 20px 5px rgb(93 68 126);\n" +
                "  display: flex;\n" +
                "  align-items: center;\n" +
                "  justify-content: center;\n" +
                "  background-color: var(--purple);\n" +
                "  position: absolute;" +
                "bottom: initial;\n" +
                "  left: 34%;" +
                "}\n" +
                "\n" +
                ".page-title {" +
                "color :azure" +
                "}" +
                ".pagination a{\n" +
                "  padding: 0px 18px;\n" +
                "  font-weight: 500;\n" +
                "  text-decoration: none;\n" +
                "  font-size: 20px;\n" +
                "  color: #9d65ad;\n" +
                "  border-radius: 50%;\n" +
                "  transition: 0.5s ease-in-out;\n" +
                "}\n" +
                ".control{\n" +
                "  font-size: 20px;\n" +
                "  font-weight: bold;\n" +
                "  background-color: transparent;\n" +
                "  color: rgb(200, 200, 239);\n" +
                "  border: none;\n" +
                "  cursor: pointer;\n" +
                "  padding: 15px 15px;\n" +
                "  border-radius: 25px;\n" +
                "  transition: 0.5s ease-in-out ;\n" +
                "}\n" +
                "\n" +
                "card-link link{" +
                "font-size: 12px;" +
                "}" +
                ".control:not(.disabled):hover,.pageNumbers a:hover{\n" +
                "  background-color:#08010a;\n" +
                "  color: white;\n" +
                "}\n" +
                "a.active{\n" +
                "  background-color:#5b3166;\n" +
                "  color: white;\n" +
                "}\n" +
                ".hidden{\n" +
                "  display: none;\n" +
                "}\n" +
                "li{\n" +
                "  list-style-type: none;\n" +
                "} \n" +
                "* {\n" +
                "  margin: 0;\n" +
                "  padding: 0;\n" +
                "  box-sizing: border-box;\n" +
                "  font-family: \"Poppins\", sans-serif;\n" +
                "}\n" +
                "body {\n" +
                "  background: #08010a;\n" +
                "height: 200%;\n" +
                "  color: blanchedalmond;\n" +
                "}\n" +
                ".nav {\n" +
                "  position: fixed;\n" +
                "  top: 0;\n" +
                "  left: 0;\n" +
                "  width: 100%;\n" +
                "  height: 14%;\n" +
                "  padding: 15px 200px;\n" +
                "  background-color:#16161cd4 ;\n" +
                "  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);\n" +
                "}\n" +
                ".nav,\n" +
                ".nav .nav-links {\n" +
                "  display: flex;\n" +
                "  align-items:center;\n" +
                "}\n" +
                ".nav {\n" +
                "  justify-content:first baseline;\n" +
                "}\n" +
                ".nav-content{\n" +
                "  display: block;\n" +
                "  \n" +
                "}\n" +
                "a {\n" +
                "  color:#caa1d5;\n" +
                "  text-decoration: none;\n" +
                "}\n" +
                ".nav .logo {\n" +
                "\n" +
                "  font-size: 30px;\n" +
                "  font-weight: 500;\n" +
                "  position: absolute;\n" +
                "  left:3%;\n" +
                "  top:30%;\n" +
                "  background: linear-gradient(to right,#0C134F,#1D267D,#5C469C,#654E92,#D4ADFC,#6C9BCF,#A5C0DD,#EBD8B2);\n" +
                "  -webkit-background-clip: text;\n" +
                "  -webkit-text-fill-color: transparent;\n" +
                "  font-weight:bolder;\n" +
                "\n" +
                "}\n" +
                ".search{\n" +
                "  position: relative;\n" +
                "  width: 700px; \n" +
                "  top:30%;\n" +
                "  right:20%;\n" +
                "  height: 60px;\n" +
                "  background:#fff;\n" +
                "  border-radius: 60px;\n" +
                "  transition: 0.5s;\n" +
                "  box-shadow:0 0 0 5px #ACB1D6;\n" +
                "  overflow: hidden;\n" +
                "  }\n" +
                "  .search .input\n" +
                "{\n" +
                "position: relative;\n" +
                "width: 300px;\n" +
                "height: 60px;\n" +
                "left: -8%;\n" +
                "size: 20px;\n" +
                "display: flex;\n" +
                "justify-content: center;\n" +
                "align-items: center;\n" +
                "top: -13%;\n" +
                "}\n" +
                "\n" +
                ".nav .nav-links {\n" +
                "  column-gap: 20px;\n" +
                "  list-style: none;\n" +
                "}\n" +
                ".nav .nav-links a {\n" +
                "  transition: all 0.2s linear;\n" +
                "}\n" +
                ".nav.openSearch .nav-links a {\n" +
                "  opacity: 0;\n" +
                "  pointer-events: none;\n" +
                "}\n" +
                ".nav .search-icon {\n" +
                "  color: #fff;\n" +
                "  font-size: 20px;\n" +
                "  cursor: pointer;\n" +
                "}\n" +
                ".nav .search-box {\n" +
                "  position: absolute;\n" +
                "  /* right: 250px; */\n" +
                "  height: 45px;\n" +
                "  left:20%;\n" +
                "  max-width: 555px;\n" +
                "  width: 100%;\n" +
                "  /* pointer-events: none; */\n" +
                "  transition: all 0.2s linear;\n" +
                "}\n" +
                ".nav.openSearch .search-box {\n" +
                "  opacity: 1;\n" +
                "  pointer-events: auto;\n" +
                "  top: auto;\n" +
                "  /* left: 20%; */\n" +
                "}\n" +
                ".search-box .search-icon {\n" +
                "  position: absolute;\n" +
                "  left: 15px;\n" +
                "  top: 50%;\n" +
                "  left: 15px;\n" +
                "  color: #4a98f7;\n" +
                "  transform: translateY(-50%);\n" +
                "}\n" +
                "/* .search-box input {\n" +
                "  height: 100%;\n" +
                "  width: 100%;\n" +
                "  border: none;\n" +
                "  outline: none;\n" +
                "  border-radius: 6px;\n" +
                "  background-color: #fff;\n" +
                "  padding: 0 15px 0 45px;\n" +
                "} */\n" +
                "\n" +
                ".nav .navOpenBtn,\n" +
                ".nav .navCloseBtn {\n" +
                "  display: none;\n" +
                "}\n" +
                "\n" +
                "/* responsive */\n" +
                "@media screen and (max-width: 1160px) {\n" +
                "  .nav {\n" +
                "    padding: 15px 100px;\n" +
                "  }\n" +
                "  .nav .search-box {\n" +
                "    right: 100px;\n" +
                "  }\n" +
                "}\n" +
                "@media screen and (max-width: 950px) {\n" +
                "  .nav {\n" +
                "    padding: 15px 7px;\n" +
                "  }\n" +
                "  .nav .search-box {\n" +
                "    right: 237px;\n" +
                "    max-width: 400px;\n" +
                "  }\n" +
                "}\n" +
                "@media screen and (max-width: 768px) {\n" +
                "  .nav .navOpenBtn,\n" +
                "  .nav .navCloseBtn {\n" +
                "    display:block;\n" +
                "  }\n" +
                "  .nav {\n" +
                "    padding: 15px 20px;\n" +
                "  }\n" +
                "  .nav  {\n" +
                "    position: fixed;\n" +
                "    top: 0;\n" +
                "    left: -100%;\n" +
                "    height: 100%;\n" +
                "    max-width: 280px;\n" +
                "    width: 100%;\n" +
                "    padding-top: 100px;\n" +
                "    row-gap: 30px;\n" +
                "    flex-direction: column;\n" +
                "    background-color: #11101d;\n" +
                "    box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);\n" +
                "    transition: all 0.4s ease;\n" +
                "    z-index: 100;\n" +
                "  }\n" +
                "  .nav.openNav .nav-links {\n" +
                "    left: 0;\n" +
                "  }\n" +
                "  .nav .navOpenBtn {\n" +
                "    color: #fff;\n" +
                "    font-size: 20px;\n" +
                "    cursor: pointer;\n" +
                "  }\n" +
                "  .nav .navCloseBtn {\n" +
                "    position: absolute;\n" +
                "    top: 20px;\n" +
                "    right: 20px;\n" +
                "    color: #fff;\n" +
                "    font-size: 20px;\n" +
                "    cursor: pointer;\n" +
                "  }\n" +
                "  .nav .search-box {\n" +
                "    top: calc(100% + 10px);\n" +
                "    max-width: calc(100% - 20px);\n" +
                "    right: 50%;\n" +
                "    transform: translateX(50%);\n" +
                "    box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);\n" +
                "    }\n" +
                "}\n" +
                "\n" +
                ".BoxofResult{\n" +
                "  display: inline-block;\n" +
                "  position: absolute;\n" +
                "  top: 92px;\n" +
                "  width: 100%;\n" +
                "  height: 213%; \n" +
                "  z-index: -1;\n" +
                "}\n" +
                "\n" +
                ".result{\n" +
                "  border: #6f42c1;\n" +
                "  border-style:ridge;\n" +
                "  border-radius: 1.25rem;\n" +
                "margin:2%;" +
                "padding:1%;" +
                "  \n" +
                "}\n" +
                "\n" +
                ".search .icon\n" +
                "{\n" +
                "position: absolute;\n" +
                "top: -3px;\n" +
                "    left: 13px;\n" +
                "    width: 30px;\n" +
                "height: 60px;\n" +
                "/*to be edited */\n" +
                "background: #ffffff;\n" +
                "border-radius: 60px;\n" +
                "display: flex;\n" +
                "justify-content: center;\n" +
                "align-items: center;\n" +
                "z-index: 1000;\n" +
                "cursor: pointer;\n" +
                "}\n" +
                ".search .icon::before\n" +
                "{\n" +
                "content: '';\n" +
                "position: absolute;\n" +
                "width: 15px;\n" +
                "height: 15px;\n" +
                "border: 3px solid #FFEAD2;\n" +
                "border-radius: 50px;\n" +
                "transform: translate(-4px,-4px);\n" +
                "}\n" +
                ".search .icon::after\n" +
                "{\n" +
                "content: '';\n" +
                "position: absolute;\n" +
                "width: 3px;\n" +
                "height: 12px;\n" +
                "background:#FFEAD2;\n" +
                "transform: translate(6px,6px) rotate(315deg);\n" +
                "\n" +
                "}\n" +
                "input {\n" +
                "  position: relative;\n" +
                "  top: -6px;\n" +
                "  left: 20%;\n" +
                "  width:fit-content;\n" +
                "  height: 100%;\n" +
                "  border: none;\n" +
                "  outline: none;\n" +
                "  font-size: 18px;\n" +
                "  margin:0px;}\n" +
                "strong {\n" +
                "font-weight:400;" +
                "    color: #caa1d5;}\n" +
                "</style>"
        );
    }
    private void addbeforresult(StringBuilder page)
    {
        page.append("<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "\n" +
                "<head>\n" +
                "  <meta charset=\"UTF-8\" />\n" +
                "  <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\" />\n" +
                "  <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\" />\n" +
                "  <title>Navigation Bar with Search Box</title>\n" +
                "  <link rel=\"stylesheet\" href=\"https://unicons.iconscout.com/release/v4.0.0/css/line.css\" />\n" +
                "  <link rel=\"stylesheet\" href=\"https://cdn.jsdelivr.net/npm/bootstrap@4.1.3/dist/css/bootstrap.min.css\" integrity=\"sha384-MCw98/SFnGE8fJT3GXwEOngsV7Zt27NXFoaoApmYm81iuXoPkFOJwJ8ERdknLPMO\" crossorigin=\"anonymous\">\n" +
                "  <script src=\"https://code.jquery.com/jquery-3.3.1.slim.min.js\" integrity=\"sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo\" crossorigin=\"anonymous\"></script>\n" +
                "<script src=\"https://cdn.jsdelivr.net/npm/popper.js@1.14.3/dist/umd/popper.min.js\" integrity=\"sha384-ZMP7rVo3mIykV+2+9J3UJ46jBk0WLaUAdn689aCwoqbBJiSnjAK/l8WvCWPIPm49\" crossorigin=\"anonymous\"></script>\n" +
                "<script src=\"https://cdn.jsdelivr.net/npm/bootstrap@4.1.3/dist/js/bootstrap.min.js\" integrity=\"sha384-ChfqqxuZUCnJSK3+MXmPNIyE6ZbWh2IMqE241rYiqJxyMiZ6OW/JmZQ5stwEULTy\" crossorigin=\"anonymous\"></script>\n" +
                "<style></style>\n" +
                "  <script src=\"script.js\" defer></script>\n" +
                "  <meta charset=\"UTF-8\" />\n" +
                "  <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\" />\n" +
                "  <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\" />\n" +
                "  <link rel=\"stylesheet\" href=\"css/bootstrap.min.css\" />\n" +
                "  <link rel=\"stylesheet\" href=\"css/all.min.css\" />\n" +
                "  <link rel=\"stylesheet\" href=\"css/main.css\" />\n" +
                "  <link rel=\"manifest\" href=\"favicon/manifest.json\" />\n" +
                "\n" +
                "\n" +
                "  <link\n" +
                "    rel=\"apple-touch-icon\"\n" +
                "    sizes=\"57x57\"\n" +
                "    href=\"favicon/apple-icon-57x57.png\"\n" +
                "  />\n" +
                "  <link\n" +
                "    rel=\"apple-touch-icon\"\n" +
                "    sizes=\"60x60\"\n" +
                "    href=\"favicon/apple-icon-60x60.png\"\n" +
                "  />\n" +
                "  <link\n" +
                "    rel=\"apple-touch-icon\"\n" +
                "    sizes=\"72x72\"\n" +
                "    href=\"favicon/apple-icon-72x72.png\"\n" +
                "  />\n" +
                "  <link\n" +
                "    rel=\"apple-touch-icon\"\n" +
                "    sizes=\"76x76\"\n" +
                "    href=\"favicon/apple-icon-76x76.png\"\n" +
                "  />\n" +
                "  <link\n" +
                "    rel=\"apple-touch-icon\"\n" +
                "    sizes=\"114x114\"\n" +
                "    href=\"favicon/apple-icon-114x114.png\"\n" +
                "  />\n" +
                "  <link\n" +
                "    rel=\"apple-touch-icon\"\n" +
                "    sizes=\"120x120\"\n" +
                "    href=\"favicon/apple-icon-120x120.png\"\n" +
                "  />\n" +
                "  <link\n" +
                "    rel=\"apple-touch-icon\"\n" +
                "    sizes=\"144x144\"\n" +
                "    href=\"favicon/apple-icon-144x144.png\"\n" +
                "  />\n" +
                "  <link\n" +
                "    rel=\"apple-touch-icon\"\n" +
                "    sizes=\"152x152\"\n" +
                "    href=\"favicon/apple-icon-152x152.png\"\n" +
                "  />\n" +
                "  <link\n" +
                "    rel=\"apple-touch-icon\"\n" +
                "    sizes=\"180x180\"\n" +
                "    href=\"favicon/apple-icon-180x180.png\"\n" +
                "  />\n" +
                "  <link\n" +
                "    rel=\"icon\"\n" +
                "    type=\"image/png\"\n" +
                "    sizes=\"192x192\"\n" +
                "    href=\"favicon/android-icon-192x192.png\"\n" +
                "  />\n" +
                "  <link\n" +
                "    rel=\"icon\"\n" +
                "    type=\"image/png\"\n" +
                "    sizes=\"32x32\"\n" +
                "    href=\"favicon/favicon-32x32.png\"\n" +
                "  />\n" +
                "  <link\n" +
                "    rel=\"icon\"\n" +
                "    type=\"image/png\"\n" +
                "    sizes=\"96x96\"\n" +
                "    href=\"favicon/favicon-96x96.png\"\n" +
                "  />\n" +
                "  <link\n" +
                "    rel=\"icon\"\n" +
                "    type=\"image/png\"\n" +
                "    sizes=\"16x16\"\n" +
                "    href=\"favicon/favicon-16x16.png\"\n" +
                "  />\n" +
                "  <meta name=\"msapplication-TileColor\" content=\"#ffffff\" />\n" +
                "  <meta name=\"msapplication-TileImage\" content=\"ms-icon-144x144.png\" />\n" +
                "  <meta name=\"theme-color\" content=\"#ffffff\" />\n" );
        addCss(page);
        page.append(
                "</head>\n" +
                        "\n" +
                        "<body>\n" +
                        "  <nav class=\"nav\">\n" +
                        "       <a href=\"http://localhost:8080/SearchEngine.html\" class=\"logo \">GOWELL</a>\n" +
                        "\n" +
                        "    <div class=\"search-box search\">\n" +
                        "          <div class=\"icon\"></div>\n" +
                        "          <form  method=\"get\" id=\"search-form\">\n" +
                        "              <div class=\"input\">\n" +
                        "                  <input type=\"search\" placeholder=\"Search\" id=\"letssearch\" name =\"Query\">\n" +
                        "              </div>\n" +
                        "          </form>\n" +
                        "          <span class=\"clear\" onclick=\"document.getElementById('letssearch').value = '' ,\n" +
                        "       document.getElementById('sentences-list').innerHTML=''\"></span>\n" +
//                        "      <button type=\"button\" id=\"voice-input-btn\"><i class=\"fas fa-microphone\"></i></button>\n" +
                        "\n" +
                        "  </div>\n" +
                        "</div>\n" +
                        "  </nav>\n" +
                        "\n" +
                        "  <div class=\"BoxofResult\">\n" +
                        "\n" +
                        "    <div class=\"container\">\n" +
                        "      <div class=\"wrapper\">\n" +
                        "          <div class=\"content\">\n" +
                        "              <ul id=\"paginationList\">\n" );


    }
    private void addafterresult(StringBuilder page){
        page.append(
                "                      </ul>\n" +
                        "          </div>\n" +
                        "      </div>\n" +
                        "\n" +
                        "\n" +
                        "      <div class=\"pagination-container d-flex justify-content-center align-bottom\"  >\n" +
                        "          <div class=\"pagination \" >\n" +
                        "              <button class=\"control\" id=\"prev\" title=\"Previous page\">\n" +
                        "                 <<<  Prev\n" +
                        "              </button>\n" +
                        "              <div class=\"pageNumbers\">\n" +
                        "              </div>\n" +
                        "              <button class=\"control\" id=\"next\" title=\"Next page\">\n" +
                        "                  Next >>>\n" +
                        "              </button>\n" +
                        "          </div>\n" +
                        "      </div>\n" +
                        "  </div>\n" );
        addjs(page);
        page.append(
                "  </div>\n" +
                        "\n" +
                        "</body>\n" +
                        "</html>");
    }
    private void addjs(StringBuilder page)
    {
        page.append("  <script>" +
                "const pageNumbers = document.querySelector(\".pageNumbers\");\n" +
                "    const paginationList = document.getElementById(\"paginationList\");\n" +
                "    const listItems = paginationList.querySelectorAll(\"li\");\n" +
                "    const prevButton = document.getElementById(\"prev\");\n" +
                "    const nextButton = document.getElementById(\"next\");\n" +
                "    \n" +
                "    const contentLimit = 10;\n" +
                "    const pageCount = Math.ceil(listItems.length / contentLimit);\n" +
                "    let currentPage = 1;\n" +
                "    \n" +
                "    const displayPageNumbers = (index) =>{\n" +
                "        const pageNumber = document.createElement(\"a\");\n" +
                "        pageNumber.innerText = index;\n" +
                "        pageNumber.setAttribute('href', \"#\");\n" +
                "        pageNumber.setAttribute(\"index\", index);\n" +
                "        pageNumbers.appendChild(pageNumber);\n" +
                "    };\n" +
                "    \n" +
                "    const getPageNumbers = ()=>{\n" +
                "        for(let i=1; i <= pageCount; i++){\n" +
                "            displayPageNumbers(i);\n" +
                "        };\n" +
                "    };\n" +
                "    \n" +
                "    const disableButton = (button) =>{\n" +
                "        button.classList.add(\"disabled\");\n" +
                "        button.setAttribute(\"disabled\", true);\n" +
                "    };\n" +
                "    \n" +
                "    const enableButton = (button) =>{\n" +
                "        button.classList.remove(\"disabled\");\n" +
                "        button.removeAttribute(\"disabled\");\n" +
                "    };\n" +
                "    \n" +
                "    const controlButtonsStatus = () =>{\n" +
                "        if(currentPage == 1){\n" +
                "            disableButton(prevButton);\n" +
                "        }\n" +
                "        else{\n" +
                "            enableButton(prevButton);\n" +
                "        }\n" +
                "        if(pageCount == currentPage){\n" +
                "            disableButton(nextButton);\n" +
                "        }\n" +
                "        else{\n" +
                "            enableButton(nextButton);\n" +
                "        }\n" +
                "    };\n" +
                "    \n" +
                "    const handleActivePageNumber = () =>{\n" +
                "        document.querySelectorAll('a').forEach((button) =>{\n" +
                "            button.classList.remove(\"active\");\n" +
                "            const pageIndex = Number(button.getAttribute(\"index\"));\n" +
                "            if(pageIndex == currentPage){\n" +
                "                button.classList.add('active');\n" +
                "            }\n" +
                "        });\n" +
                "    };\n" +
                "    \n" +
                "    const setCurrentPage = (pageNum) =>{\n" +
                "        currentPage = pageNum;\n" +
                "    \n" +
                "        handleActivePageNumber();\n" +
                "        controlButtonsStatus();\n" +
                "    \n" +
                "        const prevRange = (pageNum -1) * contentLimit;\n" +
                "        const currRange = pageNum * contentLimit;\n" +
                "    \n" +
                "        listItems.forEach((item, index) =>{\n" +
                "            item.classList.add('hidden');\n" +
                "            if(index >= prevRange && index < currRange){\n" +
                "                item.classList.remove('hidden');\n" +
                "            }\n" +
                "        });\n" +
                "    };\n" +
                "    \n" +
                "    window.addEventListener('load', ()=>{\n" +
                "        getPageNumbers();\n" +
                "        setCurrentPage(1);\n" +
                "    \n" +
                "        prevButton.addEventListener('click', ()=>{\n" +
                "            setCurrentPage(currentPage - 1);\n" +
                "        });\n" +
                "    \n" +
                "        nextButton.addEventListener(\"click\", ()=>{\n" +
                "            setCurrentPage(currentPage + 1);\n" +
                "        });\n" +
                "    \n" +
                "        document.querySelectorAll('a').forEach((button) =>{\n" +
                "            const pageIndex = Number(button.getAttribute('index'));\n" +
                "    \n" +
                "            if(pageIndex){\n" +
                "                button.addEventListener('click', ()=>{\n" +
                "                    setCurrentPage(pageIndex);\n" +
                "                });\n" +
                "            };\n" +
                "        });\n" +
                "    });" +
                "const icon = document.querySelector('.icon');\n" +
                "const search = document.querySelector('.search');\n" +
                "const input = document.querySelector('#letssearch');\n" +
                "const clear = document.querySelector('.clear');\n" +
                "icon.onclick = function(){\n" +
                "\tsearch.classList.toggle('active');}\n" +
                "\tdocument.querySelector('#letssearch').addEventListener('keydown', function(event) {\n" +
                "\t\t// console.log(\"here1\");\n" +
                "      if (event.key === 'Enter') {\n" +
                "\t\tconsole.log(event.key);\n" +
                "        performSearch(document.querySelector('#letssearch').value);\n" +
                "      }\n" +
                "    });\n" +
                "// Listen for click events on list items and use them as search queries\n" +
                "\n" +
                "    //suggestions\n" +
                "    const searchInput = document.getElementById('letssearch');\n" +
                "    const list = document.getElementById('sentences-list');\n" +
                "    // Listen for changes to the search input\n" +
                "    searchInput.addEventListener('input', () => {\n" +
                "      // Get the user's query\n" +
                "      const query = searchInput.value.toLowerCase();\n" +
                "    \n" +
                "      // If the query is empty, clear the search results and return early\n" +
                "      if (query.trim() === ''||document.getElementById('letssearch').value === '') {\n" +
                "        list.innerHTML = '';\n" +
                "        return;\n" +
                "      }    \n" +
                "      fetch('queries.txt')\n" +
                "        .then(response => response.text())\n" +
                "        .then(contents => {\n" +
                "          // Split the contents of the file into a list of sentences\n" +
                "          const sentences = contents.split(/\\r?\\n/);\n" +
                "    \n" +
                "          // Clear the previous search results\n" +
                "          list.innerHTML = '';\n" +
                "    \n" +
                "          // Iterate over the list of sentences and create a list item element for each sentence that matches the query\n" +
                "          sentences.forEach(sentence => {\n" +
                "            if (sentence.trim().toLowerCase().startsWith(query)) {\n" +
                "              const listItem = document.createElement('li');\n" +
                "              listItem.textContent = sentence.trim();\n" +
                "              list.appendChild(listItem);\n" +
                "            }\n" +
                "          });\n" +
                "        });\n" +
                "    });\n" +
                "    function performSearch(query) {\n" +
                "      // Add your code here to perform a search with the query value\n" +
                "      console.log('Performing search for: ' + query);\n" +
                "// Displaysuggestions(query);\n" +
                "\n" +
                "    }\n" +
                "    list.addEventListener('click', (event) => {\n" +
                "      // Check if the clicked element is a list item\n" +
                "      if (event.target.tagName === 'LI') {\n" +
                "        // Set the value of the input field to the text of the clicked element\n" +
                "        input.value = event.target.textContent;\n" +
                "        // Perform the search\n" +
                "        performSearch(input.value);\n" +
                "  list.innerHTML = '';\n" +
                "      }\n" +
                "    });\n" +
                "\n" +
                "//voice recongnition\n" +
                "// const serachform=document.querySelector(\"#search-containerm\");\n" +
                "const searchContainer = document.querySelector('.search-container');\n" +
                "const SpeechRecognition = window.SpeechRecognition || window.webkitSpeechRecognition;\n" +
                "if(SpeechRecognition)\n" +
                "{\n" +
                "  console.log(\"Your browser supports voice recognition\");\n" +
                "// serachform.insertAdjacentHTML(\"afterend\",'<button type=\"button\" id=\"voice-input-btn\"><i class=\"fas fa-microphone\"></i></button>');\n" +
                "searchContainer.insertAdjacentHTML('beforeend', '<button id=\"voice-input-btn\"><i class=\"fas fa-microphone\"></i></button>');\n" +
                "const micbtn=searchContainer.querySelector(\"button\");\n" +
                "const micIcon =micbtn.querySelector(\"i\");\n" +
                "const recognition=new SpeechRecognition();\n" +
                "micbtn.addEventListener(\"click\" , micBtnClick);\n" +
                "function micBtnClick(){\n" +
                "if(micIcon.classList.contains(\"fa-microphone\"))\n" +
                "{//start speech recognition\n" +
                "  recognition.start();\n" +
                "}\n" +
                "else{//STOP SPEECH RECOGNITION\n" +
                "recognition.stop();\n" +
                "}\n" +
                "\n" +
                "recognition.addEventListener(\"start\",startSpeechRecognition);\n" +
                "function startSpeechRecognition()\n" +
                "{\n" +
                "  micIcon.classList.remove(\"fa-microphone\");\n" +
                "  micIcon.classList.add(\"fa-microphone-slash\");\n" +
                "searchContainer.focus();\n" +
                "console.log(\"speech recognition active\");\n" +
                "}\n" +
                "recognition.addEventListener(\"end\",endSpeechRecognition);\n" +
                "function endSpeechRecognition()\n" +
                "{\n" +
                "  micIcon.classList.remove(\"fa-microphone-slash\");\n" +
                "micIcon.classList.add(\"fa-microphone\");\n" +
                "searchContainer.focus();\n" +
                "console.log(\"speech recognition disconnected\");\n" +
                "}\n" +
                "recognition.addEventListener(\"result\",resultofSpeechRecognition);\n" +
                "function resultofSpeechRecognition(event)\n" +
                "{ \n" +
                "  const transcript =event.results[0][0].transcript;\n" +
                "console.log(event);\n" +
                "input.value=transcript;\n" +
                "//to submit automatically without need to click enter\n" +
                "setTimeout(()=>{performSearch(transcript);},750);\n" +
                "}\n" +
                "}\n" +
                "}\n" +
                "else \n" +
                "console.log(\"Your browser doesn't support voice recognition\");\n" +
                "</script>");
    }
    private void boldQueryWords(Document Doc)
    {
        String paragraph=null;
        query = query.toLowerCase();
        List<String> queryList = Arrays.asList(query.split(" "));
        if(Doc.getString("paragraph")!=null)
            paragraph = Doc.getString("paragraph").toLowerCase();
        for (String que : queryList) {
            paragraph = paragraph.replaceAll(que, "<strong>" + que + "</strong>");
        }
        Doc.put("paragraph",paragraph);
    }

}
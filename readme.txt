
1. Ҫ����eclipse����������:

eclipse.bat

��eclipse�������ļ��ı���Ҫ��UTF-8.



2.�������������:

package.bat

�������ļ�����targetĿ¼��:
target\douyu-x.y.z.jar

(ע: x.y.z��ʵ�ʵİ汾��)




3. ÿ�θ��°汾ʱҪ�޸�������Щ�ļ�:

package.bat (��Ӧset version=��һ��)
pom.xml (��Ӧ<douyu.version>��һ��)



4. Ҫ��õ����е�����jar��������:

assembly.bat ��
mvn dependency:copy-dependencies


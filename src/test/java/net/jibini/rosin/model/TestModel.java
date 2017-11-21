package net.jibini.rosin.model;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

import net.jibini.rosin.polygon.Polygon;
import net.jibini.rosin.vector.Vector;

public class TestModel
{
	public static Polygon[] loadOBJ(String name)
	{
		try
		{
			String location = "models/" + name + ".obj";
			MaterialSet materials = MaterialSet.loadMaterialSet(name);
			
			ArrayList<Vector> vertices = new ArrayList<Vector>();
			ArrayList<Vector> normals = new ArrayList<Vector>();
			ArrayList<Face> faces = new ArrayList<Face>();
			BufferedReader reader = new BufferedReader(
					new InputStreamReader(TestModel.class.getClassLoader().getResourceAsStream(location)));
			String line;
			Material currentMaterial = new Material(new Vector(0, 0, 0), new Vector(0, 0, 0));
			
			while ((line = reader.readLine()) != null)
			{
				if (line.startsWith("v "))
				{
					float x = Float.valueOf(line.split(" ")[1]);
					float y = Float.valueOf(line.split(" ")[2]);
					float z = Float.valueOf(line.split(" ")[3]);
					
					vertices.add(new Vector(x, y, z));
				} else if (line.startsWith("vn "))
				{
					float x = Float.valueOf(line.split(" ")[1]);
					float y = Float.valueOf(line.split(" ")[2]);
					float z = Float.valueOf(line.split(" ")[3]);
					normals.add(new Vector(x, y, z));
				} else if (line.startsWith("f "))
				{
					Vector vertexIndices = new Vector(Float.valueOf(line.split(" ")[1].split("/")[0]),
							Float.valueOf(line.split(" ")[2].split("/")[0]),
							Float.valueOf(line.split(" ")[3].split("/")[0]));
					Vector normalIndices = new Vector(Float.valueOf(line.split(" ")[1].split("/")[2]),
							Float.valueOf(line.split(" ")[2].split("/")[2]),
							Float.valueOf(line.split(" ")[3].split("/")[2]));
					faces.add(new Face(vertexIndices, normalIndices, currentMaterial));
				} else if (line.startsWith("usemtl "))
				{
					currentMaterial = materials.materials.get(line.split(" ")[1]);
				}
			}
			
			reader.close();
			Polygon[] poly = new Polygon[faces.size()];
			for (int i = 0; i < poly.length; i ++)
			{
				Vector a = new Vector(vertices.get((int)faces.get(i).v.x - 1));
				Vector b = new Vector(vertices.get((int)faces.get(i).v.y - 1));
				Vector c = new Vector(vertices.get((int)faces.get(i).v.z - 1));
				poly[i] = new Polygon(a, b, c);
			}
			return poly;
		} catch (Exception ex)
		{
			ex.printStackTrace();
			return null;
		}
	}
	
	public static class Face
	{
		public Vector v = new Vector();
		public Vector n = new Vector();
		public Material m;
		
		public Face(Vector v, Vector n, Material m)
		{
			this.v = v;
			this.n = n;
			this.m = m;
		}
	}
	
	public static class MaterialSet
	{
		public HashMap<String, Material> materials = new HashMap<String, Material>();
		
		public static MaterialSet loadMaterialSet(String name) throws Exception
		{
			MaterialSet returned = new MaterialSet();
			String line;
			String currentName = "";
			String location = "models/" + name + ".mtl";
			
			BufferedReader reader = new BufferedReader(
					new InputStreamReader(TestModel.class.getClassLoader().getResourceAsStream(location)));
			line = null;
			currentName = "";
			Vector currentDiffuse = new Vector(0, 0, 0);
			Vector currentSpecular = new Vector(0, 0, 0);
			
			while ((line = reader.readLine()) != null)
			{
				if (line.startsWith("newmtl "))
				{
					currentName = line.split(" ")[1];
				} else if (line.startsWith("Kd "))
				{
					float r = Float.valueOf(line.split(" ")[1]);
					float g = Float.valueOf(line.split(" ")[2]);
					float b = Float.valueOf(line.split(" ")[3]);
					currentDiffuse = new Vector(r, g, b);
				} else if (line.startsWith("Ks "))
				{
					float r = Float.valueOf(line.split(" ")[1]);
					float g = Float.valueOf(line.split(" ")[2]);
					float b = Float.valueOf(line.split(" ")[3]);
					currentSpecular = new Vector(r, g, b);
					
					returned.materials.put(currentName, new Material(currentDiffuse, currentSpecular));
				}
			}
			
			reader.close();
			return returned;
		}
	}
	
	public static class Material
	{
		public Vector diffuse;
		public Vector specular;
		
		public Material(Vector diffuse, Vector specular)
		{
			this.diffuse = diffuse;
			this.specular = specular;
		}
	}
}
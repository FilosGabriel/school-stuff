#include <algorithm>
#include <cassert>
#include <climits>
#include <cmath>
#include <iostream>
#include <stdio.h>
#include "glut.h"

#define PI 3.14159265358979323846
#define DIM 1000
#define EPSILON 2.718281828459045
#define DEG2RAD PI/180.0

unsigned char prevKey;


class CartesianGrill
{
	CartesianGrill(int noLines, double offset = 0.1, const int mull_x = 0, const int mull_y = 0)
	{
		this->distance_l = (2.0f - 2 * offset) / (static_cast<double>(noLines) - 1);
		this->distance_c = distance_l;
		this->offset = offset;
		this->no_lines = noLines - 1;
		this->no_coll = this->no_lines;
		this->cx = -1.0 + offset + mull_x * distance_l;
		this->cy = -1.0 + offset + mull_y * distance_c;
		this->mull_x = mull_x;
		this->mull_y = mull_y;
	}

	CartesianGrill(const int no_lines, const int no_coll, const double offset = 0.1, const int mull_x = 0,
	               const int mull_y = 0)
	{
		this->distance_l = (2.0f - 2 * offset) / (static_cast<double>(no_coll) - 1);
		this->distance_c = (2.0f - 2 * offset) / (static_cast<double>(no_lines) - 1);
		this->offset = offset;
		this->no_lines = no_lines - 1;
		this->no_coll = no_coll - 1;
		this->cx = -1.0 + offset + mull_x * distance_l;
		this->cy = -1.0 + offset + mull_y * distance_c;
		this->mull_x = mull_x;
		this->mull_y = mull_y;
	}


	void draw_sys()
	{
		glColor3f(0.3, 0.3, 1);
		draw_filled_circle(calc_xy_from_pos(0, distance_l, cx), calc_xy_from_pos(0, distance_c, cy),
		                   distance_l < distance_c ? (distance_l / 3) : (distance_c / 3));
		glLineWidth(4.0);
		glColor3f(0.3, 0.3, 1);


		glBegin(GL_LINES);
		glLineWidth(4);
		glVertex2f(cx, cy);
		glVertex2f(cx, 1 - offset);
		glEnd();

		glLineWidth(4.0);
		glColor3f(0.3, 0.3, 1);
		glBegin(GL_LINES);
		glLineWidth(4);
		glVertex2f(cx, cy);
		glVertex2f(1 - offset, cy);
		glEnd();
	}


	void draw_grid()
	{
		glLineWidth(2);
		for (auto x = -1.0 + offset; x <= 1.0f - offset; x += distance_l)
		{
			draw_line_grid(x, 1.0f - offset);
		}
		draw_line_grid(1.0 - offset, 1.0 - offset);
		draw_line_grid(-1.0 + offset, 1.0 - offset);


		for (auto x = -1.0 + offset; x <= 1.0f - offset; x += distance_c)
		{
			draw_line_grid_y(x, 1.0f - offset);
		}
		draw_line_grid_y(-1.0 + offset, 1.0 - offset);
		draw_line_grid_y(1.0 - offset, 1.0 - offset);
	}


	void draw_filled_circle(GLfloat x, GLfloat y, GLfloat radius, bool filed = true)
	{
		auto amount = 40;
		GLfloat twice_pi = 2.0f * PI;
		auto x_cal = [&](const int i) { return x + (radius * cos(static_cast<float>(i) * twice_pi / amount)); };
		auto y_cal = [&](const int i) { return y + (radius * sin(static_cast<float>(i) * twice_pi / amount)); };
		glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
		glBegin(GL_POLYGON);
		for (auto i = 0; i <= amount; i++)
		{
			glVertex2f(x_cal(i), y_cal(i));
		}
		glEnd();
	}


	void draw_line_grid(const double x, const double y)
	{
		glColor3f(0, 0, 0);
		glBegin(GL_LINES);
		glVertex2f(x, y);
		glVertex2f(x, -y);
		glEnd();
	}

	void draw_line_grid_y(const double x, const double y)
	{
		glColor3f(0, 0, 0);
		glBegin(GL_LINES);
		glVertex2f(y, x);
		glVertex2f(-y, x);
		glEnd();
	}

	void draw_line(const int from_x, const int from_y, const int to_x, const int to_y) const
	{
		glLineWidth(2.0);
		glColor3f(1.0, 0.0, 0.0);
		glBegin(GL_LINES);
		glVertex2f(calc_xy_from_pos(from_x, distance_l, cx), calc_xy_from_pos(from_y, distance_c, cy));
		glVertex2f(calc_xy_from_pos(to_x, distance_l, cx), calc_xy_from_pos(to_y, distance_c, cy));
		glEnd();
	}

	double calc_xy_from_pos(const int x, const double l, const double origin) const
	{
		return origin + x * l;
	}

	void write_pixel(const int line, const int coll, bool filed = true, bool red = false)
	{
		if (red)
			glColor3f(1, 0, 0);
		else
			glColor3f(0.3, 0.3, 0.3);

		draw_filled_circle(calc_xy_from_pos(line, distance_l, cx), calc_xy_from_pos(coll, distance_c, cy),
		                   distance_l < distance_c ? (distance_l / 3) : (distance_c / 3), filed);
	}


	void draw_multiple_pixels(const int x, const int y, int length)
	{
		for (auto i = -length; i <= length; i++)
		{
			write_pixel(x + i, y + i);
			write_pixel(x, y + i);
			write_pixel(y + i, x + 1);
			write_pixel(y + i, x);
		}
	}


	void draw_line_circle(const double radius)
	{
		auto amount = 40;
		GLfloat twice_pi = PI / 2;
		auto x_cal = [&](const int i) { return cx + (radius * cos(static_cast<float>(i) * twice_pi / amount)); };
		auto y_cal = [&](const int i) { return cy + (radius * sin(static_cast<float>(i) * twice_pi / amount)); };
		glBegin(GL_LINE_STRIP);
		glColor3f(1, 0, 0);
		for (auto i = 0; i <= amount; i++)
			glVertex2f(x_cal(i), y_cal(i));
		glEnd();
	}


	void afisare_cerc_4(const int radius)
	{
		auto x = 0, y = radius;
		auto d = 1 - radius;
		auto dE = 3;
		auto dSE = -2 * radius + 5;
		auto M = EPSILON;
		draw_multiple_pixels(x, y, 1);
		while (y > x)
		{
			if (d < 0)
			{
				d += dE;
				dE += 2;
				dSE += 2;
			}
			else
			{
				d += dSE;
				dE += 2;
				dSE += 4;
				y--;
			}
			x++;
			draw_multiple_pixels(x, y, 1);
		}
	}

	void draw_ellipse(const int radius_x_int, const int radius_y_int)
	{
		auto calc_radius = [&](const int radius) { return radius * distance_c; };
		const auto radius_x = calc_radius(radius_x_int);
		const auto radius_y = calc_radius(radius_y_int);
		glColor3f(1, 0, 0);
		glBegin(GL_LINE_LOOP);
		for (auto i = 0; i < 360; i++)
		{
			const auto rad = static_cast<double>(i) * DEG2RAD;
			glVertex2f(cx + radius_x + cos(rad) * radius_x,
			           cy + radius_y + sin(rad) * radius_y);
		}
		glEnd();
		write_pixel(radius_x_int, radius_y_int, true, true);
	}

	void umplere_elipsa(const int x0, const int y0)
	{
		const auto a = x0;
		const auto b = y0;
		auto x = 0, y = -b;
		auto fxpyp = 0.0;
		write_pixel(x - x0, y + y0);

		//  1
		auto calc_delta_V_1 = [](const int x, const int b) {return (-2 * x + 1) * (b * b); };
		auto calc_delta_nv_1 = [](const int x, const int y, const int a, const int b) {return (-2 * x + 1) * (b * b) + (2 * y + 1) * (a * a); };
		auto left_comparasion = [](const int a, const int y) {return (y - 0.5) * (static_cast<double>(a)* static_cast<double>(a)); };
		auto right_comparasion = [](const int x,const int b) {return (static_cast<double>(x) + 1)* (static_cast<double>(b)* static_cast<double>(b)); };
		while (left_comparasion(a,y) < right_comparasion(x,b))
		{
			const double delta_v = calc_delta_V_1(x, b);
			const auto delta_nv = calc_delta_nv_1(x,y,a,b);
			if (fxpyp + delta_v <= 0.0)
			{
				fxpyp += delta_v;
				x--;
			}
			else if (fxpyp + delta_nv <= 0.0)
			{
				fxpyp += delta_nv;
				x--;
				y++;
			}
			for (auto i = x + x0; i <= x0; i++)
			{
				write_pixel(i, y + y0);
			}
		}


		//2
		auto calc_delta_nv_2 = [](const int a, const int b, const int x, const int y) {return b * b * (-2 * x + 1) + a * a * (2 * y + 1); };
		auto calc_delta_n_2 = [](const int a ,const int y) {return a * a * (2 * y + 1); };
		while (y < 0)
		{
			const auto delta_nv = calc_delta_nv_2(a,b,x,y);
			const double delta_n =calc_delta_n_2(a,y) ;
			if (fxpyp + delta_nv <= 0.0)
			{
				fxpyp += delta_nv;
				x--;
				y++;
			}
			else
			{
				fxpyp += delta_n;
				y++;
			}
			for (auto i = x + x0; i <= x0; i++)
			{
				write_pixel(i, y + y0);
			}
		}
	}


	void show()
	{
		auto calc_radius = [&](int radius) { return radius * distance_c; };
		draw_grid();
		draw_sys();
		auto radius = 25;
		draw_line_circle(calc_radius(radius));
		afisare_cerc_4(radius);
	}

	void show2()
	{
		draw_grid();
		draw_sys();
		umplere_elipsa(13, 7);
		draw_ellipse(13, 7);
	}


public:
	int no_lines;
	int no_coll;
	double distance_l;
	double distance_c;
	double start_position;
	double offset = 0.1;

	double cx;
	double cy;
	int mull_x;
	int mull_y;

	static void render()
	{
		glClearColor(1.0, 1.0, 1.0, 1.0);
		glutPostRedisplay();
		glClear(GL_COLOR_BUFFER_BIT);
		auto cartesian_grill = new CartesianGrill(30);
		cartesian_grill->show();
	}

	static void render2()
	{
		glClearColor(1.0, 1.0, 1.0, 1.0);
		glutPostRedisplay();
		glClear(GL_COLOR_BUFFER_BIT);
		auto cartesian_grill = new CartesianGrill(30);
		cartesian_grill->show2();
	}
};


void display(void)
{
	if (prevKey == '1')
		CartesianGrill::render();
	else if (prevKey == '2')
		CartesianGrill::render2();
	glFlush();
}

void Reshape(int w, int h)
{
	glViewport(0, 0, (GLsizei)w, (GLsizei)h);
}

void KeyboardFunc(unsigned char key, int x, int y)
{
	prevKey = key;
	if (key == 27) // escape
		exit(0);
	glutPostRedisplay();
}

void MouseFunc(int button, int state, int x, int y)
{
}

void Init(void)
{
	glClearColor(1.0, 1.0, 1.0, 1.0);


	glLineWidth(1);

	glPointSize(3);

	glPolygonMode(GL_FRONT, GL_LINE);
}


int main(int argc, char** argv)
{
	prevKey = '2';
	glutInit(&argc, argv);

	glutInitWindowSize(DIM, DIM);

	glutInitWindowPosition(100, 100);

	glutInitDisplayMode(GLUT_SINGLE | GLUT_RGB);

	glutCreateWindow(argv[0]);

	Init();

	glutReshapeFunc(Reshape);

	glutKeyboardFunc(KeyboardFunc);

	glutMouseFunc(MouseFunc);

	glutDisplayFunc(display);

	glutMainLoop();

	return 0;
}

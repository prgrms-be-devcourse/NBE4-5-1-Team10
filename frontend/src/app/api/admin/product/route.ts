import { NextRequest, NextResponse } from "next/server";
import { cookies } from "next/headers";
import client from "@/lib/backend/client";
import { components } from "@/lib/backend/generated/schema";

type CartResponse = {
  code?: string;
  msg?: string;
  data?: components["schemas"]["CartDto"];
};

export async function POST(req: NextRequest) {
  const token = (await cookies()).get("accessToken");
  if (!token) {
    return NextResponse.json({ error: "No token" }, { status: 401 });
  }

  const body = await req.json();
  const { name, description, price, imageUrl, stockQuantity } = body as {
    name: string;
    description: string;
    price: number;
    imageUrl: string;
    stockQuantity: number;
  };

  const response = await client.POST("/api/v1/admin/product", {
    headers: {
      Authorization: `Bearer ${token.value}`,
    },
    body: { name, description, price, imageUrl, stockQuantity },
    credentials: "include",
  });

  if (response.error) {
    return NextResponse.json({ error: response["error"] }, { status: 400 });
  }

  const getProduct = response.data as CartResponse;
  return NextResponse.json(getProduct);
}

export async function PUT(req: NextRequest) {
  const token = (await cookies()).get("accessToken");
  if (!token) {
    return NextResponse.json({ error: "No token" }, { status: 401 });
  }

  const body = await req.json();
  const { id, name, description, price, imageUrl, stockQuantity } = body as {
    id: number;
    name: string;
    description: string;
    price: number;
    imageUrl: string;
    stockQuantity: number;
  };

  const response = await client.PUT("/api/v1/admin/product/{id}", {
    params: {
      path: {
        id,
      },
    },
    headers: { Authorization: `Bearer ${token.value}` },
    body: { name, description, price, imageUrl, stockQuantity },
    credentials: "include",
  });
  if (response.error) {
    return NextResponse.json({ error: response["error"] }, { status: 400 });
  }
  const updatedProduct = response.data as CartResponse;
  return NextResponse.json(updatedProduct);
}

export async function DELETE(req: NextRequest) {
  const token = (await cookies()).get("accessToken");
  if (!token) {
    return NextResponse.json({ error: "No token" }, { status: 401 });
  }

  const body = await req.json();
  const { id } = body as {
    id: number;
  };

  const response = await client.DELETE("/api/v1/admin/product/{id}", {
    params: {
      path: {
        id,
      },
    },
    headers: { Authorization: `Bearer ${token.value}` },
    credentials: "include",
  });
  if (response.error) {
    return NextResponse.json({ error: response["error"] }, { status: 400 });
  }
  return NextResponse.json({ success: true }, { status: 200 });
}

import { CartItemDto } from "@/app/order/process/page";
import client from "@/lib/backend/client";
import { cookies } from "next/headers";
import { NextRequest, NextResponse } from "next/server";

export async function POST(req: NextRequest) {
    const token = (await cookies()).get("accessToken");
    if (!token) {
      return NextResponse.json({ error: "No token" }, { status: 401 });
    }
  
    const body = await req.json();
    const { address, postalCode, shippingPrice, items } = body as {
        address: string,
        postalCode: string,
        shippingPrice: number,
        items: CartItemDto[]
    };
  
    const response = await client.POST("/api/v1/order", {
      headers: { Authorization: `Bearer ${token.value}` },
      body: {
        address,
        postalCode,
        shippingPrice,
        orderItems: items.map(item => {
            return {
              productId: item.productId,
              price: item.totalPrice,
              quantity: item.quantity
            }
        })
      },
      credentials: "include",
    });
    if (response.error) {
      return NextResponse.json({ error: response["error"] }, { status: 400 });
    }
    const addedToCart = response.data;
    return NextResponse.json(addedToCart);
  }